package com.dailystudy.swinglab.service.business.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class JwtToken
{
    private String accessToken;
    private String refreshToken;

    @JsonIgnore
    private int refreshExpSec;
}
