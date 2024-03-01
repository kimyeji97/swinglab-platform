package com.dailystudy.swinglab.service.business.user.service;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.Ticket;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.TicketQueryRepository;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.business.common.service.BaseService;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabServiceUnavailableException;
import com.dailystudy.swinglab.service.framework.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketValidationService extends BaseService
{
    private final UserRepository userRepository;
    private final TicketQueryRepository ticketQueryRepository;

    /**
     * 티켓 가지고 있는지 확인
     */
    public void assertHaveTicket ()
    {
        Long userId = SecurityUtil.getUserId();
        User user = userRepository.findById(userId).get();
        if (user.getTicketId() == null)
        {
            throw new SwinglabBadRequestException("등록된 이용권이 없습니다.");
        }

        Ticket ticket = ticketQueryRepository.findNowTicketByUserId(userId, user.getTicketId());
        if (ticket == null)
        {
            throw new SwinglabBadRequestException("등록된 이용권이 없습니다.");
        }

        LocalDate nowDate = LocalDate.now();
        if ((ticket.getSvcStDay().isBefore(nowDate) && ticket.getSvcEdDay().isAfter(LocalDate.now())) == false)
        {
            throw new SwinglabServiceUnavailableException("이용권이 만료되었습니다.");
        }
    }
}
