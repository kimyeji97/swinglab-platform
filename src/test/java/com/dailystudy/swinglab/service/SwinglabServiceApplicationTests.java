package com.dailystudy.swinglab.service;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneBookHist;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class SwinglabServiceApplicationTests
{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    //	@Test
    public void insertUser ()
    {
        User user = new User();
        user.setName("test");
        user.setPwd("qhdks@00");
        user.setPwdChk("qhdks@00");
        user.setDelYn(false);
        user.setLoginId("test");
        userRepository.save(user);
    }

    @Test
    public ZoneBookHist testGetOne ()
    {
        return null;
    }
}
