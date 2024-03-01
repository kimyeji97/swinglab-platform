package com.dailystudy.swinglab.service.business.common.service;

import com.dailystudy.swinglab.service.business.common.domain.MainData;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.Ticket;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.Zone;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneBookHist;
import com.dailystudy.swinglab.service.business.common.repository.user.TicketQueryRepository;
import com.dailystudy.swinglab.service.business.common.repository.zone.ZoneBookHistQueryRepository;
import com.dailystudy.swinglab.service.business.common.repository.zone.ZoneRepository;
import com.dailystudy.swinglab.service.business.user.service.TicketService;
import com.dailystudy.swinglab.service.business.user.service.UserValidationService;
import com.dailystudy.swinglab.service.framework.core.gen.entity.ZoneCore;
import com.dailystudy.swinglab.service.framework.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService extends BaseService
{
    private final UserValidationService userValidationService;
    private final TicketService ticketService;
    private final ZoneRepository zoneRepository;
    private final ZoneBookHistQueryRepository zoneBookHistQueryRepository;
    private final TicketQueryRepository ticketQueryRepository;

    /**
     * 메인 데이터
     *
     * @return
     */
    public MainData getMainData ()
    {
        Long userId = SecurityUtil.getUserId();
        // 유저 정보
        User user = userValidationService.getValidUser(userId);
        // 이용권 정보
        Ticket ticket = user.getTicketId() != null
                ? ticketQueryRepository.findNowTicketByUserId(userId, user.getTicketId())
                : null;
        // 예약 정보
        ZoneBookHist bookHist = zoneBookHistQueryRepository.findNextOneByUserId(userId);
        if (bookHist != null)
        {
            Optional<Zone> zone = zoneRepository.findById(bookHist.getZoneId());
            bookHist.setZoneNm(zone.map(ZoneCore::getZoneNm).orElse(null));
            bookHist.setIsMyBook(true);
        }
        return new MainData(user, ticket, bookHist);
    }
}
