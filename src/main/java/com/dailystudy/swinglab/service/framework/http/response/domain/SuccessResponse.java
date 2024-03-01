package com.dailystudy.swinglab.service.framework.http.response.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
public class SuccessResponse<R>
{
    private final String resultCode = "S";
    private R data;

    @JsonInclude(Include.NON_NULL)
    private PaginationResponse pagination;

    public SuccessResponse()
    {
    }

    public SuccessResponse(R data)
    {
        if (data != null)
        {
            this.data = data;
        } else
        {
            this.data = null;
        }
    }

    public SuccessResponse(R data, PaginationResponse pagination)
    {
        this.data = data;
        this.pagination = pagination;
    }
}
