package com.dailystudy.swinglab.service.business.auth.service;

import com.dailystudy.swinglab.service.business.common.service.BaseService;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.utils.StringValidUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService extends BaseService
{
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthValidationService authValidationService;

    private final UserRepository userRepository;


    /**
     * 회워가입
     *
     * @param param
     * @return
     */
    public User signupUser (User param)
    {
        /*
         * 유효성 검사
         */
        // 필수값 체크
        assertNotEmpty(param, "가입 정보");
        assertNotBlank(param.getLoginId(), "로그인 Id");
        assertNotBlank(param.getName(), "이름");
        assertNotBlank(param.getPwd(), "비밀번호");
        assertNotBlank(param.getPwdChk(), "비밀번호 확인");

        // 로그인아이디 중복 체크
        authValidationService.assertNotExistLoginId(null, param.getLoginId());

        // 비밀번호 체크
        String pwd = new String(Base64.decodeBase64(param.getPwd()));
        String pwdChk = new String(Base64.decodeBase64(param.getPwdChk()));
        authValidationService.validatePwd(pwd, pwdChk);

        /*
         * 데이터 저장
         */
        // 기본값 세팅
        if (StringUtils.isBlank(param.getNickNm()))
        {
            param.setNickNm(param.getName());
        }
        param.setSignupDt(LocalDateTime.now());
        param.setDelYn(false);
        param.setPwd(bCryptPasswordEncoder.encode(pwd));

        // [INSERT] TB_USER
        User result = userRepository.save(param);
        result.setPwd(null);
        result.setPwdChk(null);
        return result;
    }
}
