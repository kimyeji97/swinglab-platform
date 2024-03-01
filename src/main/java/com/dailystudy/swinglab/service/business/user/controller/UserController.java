package com.dailystudy.swinglab.service.business.user.controller;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.user.service.UserService;
import com.dailystudy.swinglab.service.framework.http.response.PlatformResponseBuilder;
import com.dailystudy.swinglab.service.framework.http.response.domain.SuccessResponse;
import com.dailystudy.swinglab.service.framework.http.uris.UserUriConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    /**
     * 내 정보 조회
     *
     * @return
     */
    @GetMapping(UserUriConst.GET_MY_INFO)
    public ResponseEntity<SuccessResponse<User>> getMyInfo ()
    {
        return PlatformResponseBuilder.build(userService.getUser());
    }

    /**
     * 내 닉네임 수정
     *
     * @param user
     * @return
     */
    @PutMapping(UserUriConst.PUT_MY_INFO_NICKNAME)
    public ResponseEntity<SuccessResponse<User>> putMyInfoNickName (@RequestBody User user)
    {
        return PlatformResponseBuilder.build(userService.modifyUserNickName(user));
    }

    /**
     * 내 비밀번호 수정
     *
     * @param user
     * @return
     */
    @PutMapping(UserUriConst.PUT_MY_INFO_PWD)
    public ResponseEntity<SuccessResponse> putMyInfoPwd (@RequestBody User user)
    {
        userService.modifyUserPwd(user);
        return PlatformResponseBuilder.build();
    }
}
