package com.dailystudy.swinglab.service.business.common.repository.user;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.framework.utils.ArrayUtil;
import com.dailystudy.swinglab.service.framework.utils.ListUtil;
import com.dailystudy.swinglab.service.framework.utils.ObjectUtil;
import com.querydsl.core.util.ArrayUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.dailystudy.swinglab.service.business.common.domain.entity.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository
{
    private final JPAQueryFactory jpaQueryFactory;

    public User findOneByUserId (Long userId)
    {
        return jpaQueryFactory
                .selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchOne();
    }

    public void updateTicketIdByUserId (Long userId, Long ticketId)
    {
        jpaQueryFactory
                .update(user)
                .set(user.ticketId, ticketId)
                .set(user.updDt, LocalDateTime.now())
                .where(user.userId.eq(userId))
                .execute();
    }

    /**
     * 유저 아이디 리스트로 조회
     *
     * @param userIdList
     * @return
     */
    public List<User> findAllByUserIdList (List<Long> userIdList)
    {
        if (ObjectUtils.isEmpty(userIdList))
        {
            return new ArrayList<>();
        }
        return jpaQueryFactory.selectFrom(user)
                .where(user.userId.in(userIdList),
                        user.delYn.isFalse())
                .fetch();

    }
}
