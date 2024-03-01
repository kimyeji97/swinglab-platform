package com.dailystudy.swinglab.service.business.auth.service;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.framework.auth.SwinglabUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SwinglabUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String loginId) throws UsernameNotFoundException
    {
        User user = userRepository.findByLoginIdAndDelYnFalse(loginId);
        if (user == null)
        {
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return new SwinglabUserDetail(String.valueOf(user.getUserId()), user.getPwd(), user);
    }
}
