package com.dailystudy.swinglab.service.business.user.service;

import com.dailystudy.swinglab.service.business.auth.service.AuthValidationService;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.business.common.service.BaseService;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BaseService
{
    private final UserValidationService userValidationService;
    private final AuthValidationService authValidationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    /**
     * 내 정보 조회
     *
     * @return
     * @throws AuthenticationException
     */
    public User getUser ()
    {
        Long userId = SecurityUtil.getUserId();
        return userValidationService.getValidUser(userId);
    }

    /**
     * 닉네임 정보 수정
     *
     * @param param
     * @return
     */
    public User modifyUserNickName (User param)
    {
        Long userId = SecurityUtil.getUserId();
        assertNotBlank(param.getNickNm(), "닉네임");

        User user = userValidationService.getValidUser(userId);
        user.setNickNm(param.getNickNm());
        return userRepository.save(user);
    }

    /**
     * 사용자 비밀번호 수정
     *
     * @param param
     */
    public void modifyUserPwd (User param)
    {
        Long userId = SecurityUtil.getUserId();
        assertNotBlank(param.getCrntPwd(), "현재 비밀번호");
        assertNotBlank(param.getPwd(), "변경 비밀번호");
        assertNotBlank(param.getPwdChk(), "변경 비밀번호 확인");

        String crntPwd = new String(Base64.decodeBase64(param.getCrntPwd()));
        String pwd = new String(Base64.decodeBase64(param.getPwd()));
        String pwdChk = new String(Base64.decodeBase64(param.getPwdChk()));

        /*
         * 유효성 검사
         */
        // 변경 비밀번호
        authValidationService.validatePwd(pwd, pwdChk);
        // 유저 존재 확인
        User user = userValidationService.getValidUser(userId);
        // 현재 비밀번호 일치 확인
        boolean isMatches = bCryptPasswordEncoder.matches(crntPwd, user.getPwd());
        if (isMatches == false)
        {
            throw new SwinglabBadRequestException("현재 비밀번호가 일치하지 않습니다.");
        }

        /*
         * 사용자 정보 수정
         */
        user.setPwd(bCryptPasswordEncoder.encode(pwd));
        userRepository.save(user);
    }
}
