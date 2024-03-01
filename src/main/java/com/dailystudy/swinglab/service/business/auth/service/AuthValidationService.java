package com.dailystudy.swinglab.service.business.auth.service;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.business.common.service.BaseService;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.utils.StringValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthValidationService extends BaseService
{
    private final UserRepository userRepository;

    /**
     * 로그인 아이디 중복 체크
     *
     * @param userSid
     * @param loginId
     */
    public void assertNotExistLoginId (Long userSid, String loginId)
    {
        List<User> list = userSid == null
                ? userRepository.findAllByLoginIdAndDelYnFalse(loginId)
                : userRepository.findAllByLoginIdAndUserIdNotAndDelYnFalse(loginId, userSid);
        if (list.isEmpty() == false)
        {
            throw new SwinglabBadRequestException("해당 '로그인ID'는 이미 존재합니다.");
        }
    }

    /**
     * 비밀번호 유효성 검사
     *
     * @param pwd
     * @param pwdChk
     */
    public void validatePwd (String pwd, String pwdChk)
    {
        // 비밀번호 확인 일치 여부 체크
        if (pwdChk != null && pwd.equals(pwdChk) == false)
        {
            throw new SwinglabBadRequestException("비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호 규격 체크
        if (StringValidUtil.isInValidPwd(pwd))
        {
            throw new SwinglabBadRequestException("비밀번호는 '영문, 숫자, !, @'만 가능하며 최소 4자 ~ 최대 10자까지 가능합니다.");
        }
    }
}
