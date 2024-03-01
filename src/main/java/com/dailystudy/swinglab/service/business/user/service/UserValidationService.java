package com.dailystudy.swinglab.service.business.user.service;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.business.common.service.BaseService;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService extends BaseService
{
    private final UserRepository userRepository;

    /**
     * 존재하는 유저인지 확인 후 리턴
     *
     * @param userId
     * @return
     */
    public User getValidUser (Long userId)
    {
        User user = userRepository.findByUserIdAndDelYnFalse(userId);
        if (user == null)
        {
            throw new SwinglabNotFoundException("사용자 정보를 찾을 수 없습니다.");
        }
        return user;
    }
}
