package com.dailystudy.swinglab.service.framework.http.response;

import com.dailystudy.swinglab.service.framework.http.response.domain.SuccessResponse;
import org.springframework.http.ResponseEntity;

public interface PlatformResponseWrapper
{
    ResponseEntity wrap(SuccessResponse val);
}
