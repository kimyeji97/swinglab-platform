package com.dailystudy.swinglab.service.framework.http.response;

import com.dailystudy.swinglab.service.framework.http.request.HttpRequestThreadLocal;
import com.dailystudy.swinglab.service.framework.http.response.domain.PaginationResponse;
import com.dailystudy.swinglab.service.framework.http.response.domain.SuccessResponse;
import com.dailystudy.swinglab.service.framework.utils.JsonUtil;
import com.dailystudy.swinglab.service.framework.utils.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class PlatformResponseBuilder implements InitializingBean
{
    @Autowired(required = false)
    private PlatformResponseWrapper templateWrapper;

    private static PlatformResponseWrapper wrapper;

    private final static PlatformResponseWrapper defaultWrapper = val -> new ResponseEntity(val, HttpStatus.OK);

    public static ResponseEntity<SuccessResponse> build ()
    {
        SuccessResponse val = new SuccessResponse();
        setResponseBodyThreadLocal(val);
        if (wrapper == null)
        {
            return defaultWrapper.wrap(val);
        } else
        {
            return wrapper.wrap(val);
        }
    }

    public static <T> ResponseEntity<SuccessResponse<List<T>>> build (Page<T> data)
    {
        SuccessResponse<List<T>> val = new SuccessResponse<>(data.getContent(), new PaginationResponse(data.getPageable(), data.getNumberOfElements(), (int) data.getTotalElements()));
        setResponseBodyThreadLocal(val);
        if (wrapper == null)
        {
            return defaultWrapper.wrap(val);
        } else
        {
            return wrapper.wrap(val);
        }
    }

    public static <T> ResponseEntity<SuccessResponse<T>> build (T data)
    {
        SuccessResponse<T> val = new SuccessResponse<T>(data);
        setResponseBodyThreadLocal(val);
        if (wrapper == null)
        {
            return defaultWrapper.wrap(val);
        } else
        {
            return wrapper.wrap(val);
        }
    }

    public static <T> ResponseEntity<SuccessResponse<T>> build (T data, PaginationResponse page)
    {
        SuccessResponse<T> val = new SuccessResponse<T>(data, page);
        setResponseBodyThreadLocal(val);
        if (wrapper == null)
        {
            return defaultWrapper.wrap(val);
        } else
        {
            return wrapper.wrap(val);
        }
    }

    public static <T> ResponseEntity<SuccessResponse<T>> build (SuccessResponse<T> val)
    {
        setResponseBodyThreadLocal(val);
        if (wrapper == null)
        {
            return defaultWrapper.wrap(val);
        } else
        {
            return wrapper.wrap(val);
        }
    }

    private static void setResponseBodyThreadLocal (Object val)
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            HttpRequestThreadLocal.setResponseBody(objectMapper.writeValueAsString(val));
        } catch (JsonProcessingException e)
        {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterPropertiesSet () throws Exception
    {
        PlatformResponseBuilder.wrapper = templateWrapper;
    }
}
