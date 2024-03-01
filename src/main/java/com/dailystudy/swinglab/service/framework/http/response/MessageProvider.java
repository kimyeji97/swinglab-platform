package com.dailystudy.swinglab.service.framework.http.response;

public interface MessageProvider
{
    public String getMessage(String messageCode, String... parameters);

    public String getErrorMessage(String messageCode, String... parameters);
}
