package com.dailystudy.swinglab.service.business.common.repository.user;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.Ticket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dailystudy.swinglab.service.business.common.domain.entity.user.QTicket.ticket;

@Repository
@RequiredArgsConstructor
public class TicketQueryRepository
{
    private final JPAQueryFactory jpaQueryFactory;

    public Ticket findNowTicketByUserId(Long userId, Long ticketId)
    {
        return jpaQueryFactory
                .selectFrom(ticket)
                .where(ticket.userId.eq(userId)
                    , ticket.ticketId.eq(ticketId)
                ).fetchOne();
    }
}
