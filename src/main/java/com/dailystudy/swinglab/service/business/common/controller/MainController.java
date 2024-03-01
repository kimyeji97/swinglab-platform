package com.dailystudy.swinglab.service.business.common.controller;

import com.dailystudy.swinglab.service.business.common.domain.MainData;
import com.dailystudy.swinglab.service.business.common.service.MainService;
import com.dailystudy.swinglab.service.framework.http.response.PlatformResponseBuilder;
import com.dailystudy.swinglab.service.framework.http.response.domain.SuccessResponse;
import com.dailystudy.swinglab.service.framework.http.uris.CommonUriConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MainController
{
    private final MainService mainService;

    @GetMapping(CommonUriConst.GET_MAIN_DATA)
    public ResponseEntity<SuccessResponse<MainData>> getMainData ()
    {
        return PlatformResponseBuilder.build(mainService.getMainData());
    }
}
