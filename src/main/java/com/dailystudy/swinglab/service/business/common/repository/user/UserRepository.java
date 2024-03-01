package com.dailystudy.swinglab.service.business.common.repository.user;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{
    List<User> findAllByLoginIdAndDelYnFalse(String loginId);
    List<User> findAllByLoginIdAndUserIdNotAndDelYnFalse(String loginId, Long userSid);
    User findByLoginIdAndDelYnFalse(String loginId);
    User findByUserIdAndDelYnFalse(Long userId);
}
