package com.dailystudy.swinglab.service.business.zone.service;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.Zone;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneBookHist;
import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneUsageHist;
import com.dailystudy.swinglab.service.business.common.repository.user.UserQueryRepository;
import com.dailystudy.swinglab.service.business.common.repository.zone.*;
import com.dailystudy.swinglab.service.business.common.service.BaseService;
import com.dailystudy.swinglab.service.business.user.service.TicketValidationService;
import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.http.response.exception.http.SwinglabBadRequestException;
import com.dailystudy.swinglab.service.framework.utils.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZoneService extends BaseService
{
    private final TicketValidationService ticketValidationService;
    private final ZoneValidService zoneValidService;

    private final UserQueryRepository userQueryRepository;
    private final ZoneRepository zoneRepository;
    private final ZoneBookHistRepository zoneBookHistRepository;
    private final ZoneBookHistQueryRepository zoneBookHistQueryRepository;
    private final ZoneUsageHistRepository zoneUsageHistRepository;
    private final ZoneUsageHistQueryRepository zoneUsageHistQueryRepository;

    /**
     * 타석 목록 조회
     *
     * @return
     */
    public List<Zone> getZoneAllList ()
    {
        return zoneRepository.findAll(Sort.by(Sort.Direction.DESC, "regDt"));
    }

    /**
     * 타석 상세 조회
     *
     * @param zoneSid
     * @return
     */
    public Zone getZoneDetail (Long zoneSid)
    {
        this.assertNotEmpty(zoneSid, "타석 ID");

        // TODO 금일 예약 가능 여부
        // TODO 금일 내 예약 여부

        return zoneValidService.getValidZone(zoneSid);
    }

    /**
     * 전체 예약 현황 조회
     *
     * @param param
     * @return
     */
    public List<ZoneBookHist> getZoneBookList (ZoneBookHist param)
    {
        Long userId = SecurityUtil.getUserId();

        /*
         * 데이터 조회
         */
        // 파라미터 기본 세팅
        if (param.getBookDaySt() == null)
        {
            param.setBookDaySt(LocalDate.now());
        }
        if (param.getBookDayEd() == null)
        {
            param.setBookDayEd(param.getBookDaySt().plusDays(7));
        }
        param.setBookCnclYn(false);
        // 예약 목록 조회
        List<ZoneBookHist> result = zoneBookHistQueryRepository.findAllByWhere(param);
        if (result.isEmpty())
        {
            return result;
        }
        // 타석 조회
        List<Zone> zoneList = zoneRepository.findAll();
        // 유저 조회
        List<Long> userIdList = result.stream().map(ZoneBookHist::getUserId).toList();
        List<User> userList = userQueryRepository.findAllByUserIdList(userIdList);

        /*
         * 데이터 세팅
         */
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
        Map<Long, Zone> zoneMap = zoneList.stream().collect(Collectors.toMap(Zone::getZoneId, Function.identity()));
        for (ZoneBookHist zoneBookHist : result)
        {
            zoneBookHist.setZoneNm(zoneMap.get(zoneBookHist.getZoneId()).getZoneNm());
            zoneBookHist.setNickNm(userMap.get(zoneBookHist.getUserId()) == null ? SwinglabConst.UNKNOWN : userMap.get(zoneBookHist.getUserId()).getNickNm());
            zoneBookHist.setIsMyBook(userId.equals(zoneBookHist.getUserId()));

            this.setBookStatus(zoneBookHist);
        }

        return result;
    }

    /**
     * 해당 타석 예약
     *
     * @param zoneId
     * @param bookHist
     * @return
     */
    public ZoneBookHist bookAtZone (Long zoneId, ZoneBookHist bookHist)
    {
        Long userSid = SecurityUtil.getUserId();

        // 유효성 검사
        assertNotEmpty(bookHist, "예약정보");
        assertNotEmpty(zoneId, "타석 ID");
        assertNotEmpty(bookHist.getBookStDt(), "예약 시간(시작)");
        assertNotEmpty(bookHist.getBookEdDt(), "예약 시간(종료)");

        // 존재하는 타석인지 확인
        Zone zone = zoneValidService.getValidZone(zoneId);

        // 이용권 보유 여부 체크
        ticketValidationService.assertHaveTicket();

        // 예약 가능한지 체크
        zoneValidService.validateCanBook(zoneId, bookHist);

        // 데이터 저장
        bookHist.setUserId(userSid);
        bookHist.setZoneId(zoneId);
        bookHist.setBookCnclYn(false);
        bookHist.setAutoBookCnclYn(false);
        bookHist = zoneBookHistRepository.save(bookHist);
        bookHist.setZoneNm(zone.getZoneNm());

        this.setBookStatus(bookHist);
        this.setMyBookInfo(bookHist);
        return bookHist;
    }

    /**
     * 예약 가능한지 체크
     *
     * @param zoneId
     * @param bookHist
     */
    public void validateBookAtZone (Long zoneId, ZoneBookHist bookHist)
    {
        try
        {
            // 유효성 검사
            assertNotEmpty(bookHist, "예약정보");
            assertNotEmpty(zoneId, "타석 ID");
            assertNotEmpty(bookHist.getBookStDt(), "예약 시간(시작)");
            assertNotEmpty(bookHist.getBookEdDt(), "예약 시간(종료)");

            // 존재하는 타석인지 확인
            zoneValidService.getValidZone(zoneId);

            // 예약 가능한지 체크
            zoneValidService.validateCanBook(zoneId, bookHist);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new SwinglabBadRequestException("예약 할 수 없습니다.");
        }
    }

    /**
     * 예약 취소
     *
     * @param bookId
     * @return
     */
    @Transactional
    public ZoneBookHist cancleBook (Long bookId)
    {
        // 존재하는 예약건인지 (내예약인지)
        ZoneBookHist zoneBookHist = zoneValidService.getValidBookHist(bookId);

        // 예약 취소 가능여부
        zoneValidService.assertCanCancelBook(zoneBookHist);

        // 예약 취소
        zoneBookHistQueryRepository.updateBookCnclYnTrueByKey(bookId);

        zoneBookHist = zoneBookHistQueryRepository.findOneByKey(bookId);
        this.setBookStatus(zoneBookHist);
        this.setMyBookInfo(zoneBookHist);
        return zoneBookHist;
    }

    /**
     * 입실 처리
     *
     * @param bookId
     * @return
     */
    public ZoneUsageHist checkInBook (Long bookId, Long userId)
    {
        // 존재하는 예약건인지 (내예약인지)
        ZoneBookHist zoneBookHist = zoneValidService.getValidBookHist(bookId, userId);

        // 이미 입실되었는지
        ZoneUsageHist zoneUsageHist = zoneUsageHistQueryRepository.findOneByKey(bookId);
        if (zoneUsageHist != null)
        {
            throw new SwinglabBadRequestException("이미 입실하셨습니다.");
        }

        // 입실 가능 여부
        zoneValidService.assertCanCheckIn(zoneBookHist);

        // 입실 처리
        zoneUsageHist = ZoneUsageHist.builder().build();
        zoneUsageHist.setBookId(bookId);
        zoneUsageHist.setChkInDt(LocalDateTime.now());

        zoneUsageHist = zoneUsageHistRepository.save(zoneUsageHist);
        this.setMyBookInfo(zoneUsageHist);
        return zoneUsageHist;
    }

    /**
     * 이용 내역 조회
     *
     * @param param
     * @return
     */
    public List<ZoneBookHist> getMyZoneUsageHist (ZoneBookHist param)
    {
        Long userId = SecurityUtil.getUserId();

        /*
         * 데이터 조회
         */
        // 파라미터 기본 세팅
        if (param.getBookDayEd() == null)
        {
            param.setBookDayEd(LocalDate.now());
        }
        if (param.getBookDaySt() == null)
        {
            param.setBookDaySt(param.getBookDayEd().minusDays(7));
        }
        param.setUserId(userId);
        List<ZoneBookHist> result = zoneBookHistQueryRepository.findAllByWhere(param);
        if (result == null)
        {
            return result;
        }
        // 타석 조회
        List<Zone> zoneList = zoneRepository.findAll();
        // 이용 이력 조회
        List<Long> bookIdList = result.stream().map(ZoneBookHist::getBookId).toList();
        List<ZoneUsageHist> usageHistList = zoneUsageHistQueryRepository.findListByBookIdList(bookIdList);

        /*
         * 데이터 세팅
         */
        Map<Long, ZoneUsageHist> usageHistMap = usageHistList.stream().collect(Collectors.toMap(ZoneUsageHist::getBookId, Function.identity()));
        Map<Long, Zone> zoneMap = zoneList.stream().collect(Collectors.toMap(Zone::getZoneId, Function.identity()));
        for (ZoneBookHist zoneBookHist : result)
        {
            zoneBookHist.setZoneNm(zoneMap.get(zoneBookHist.getZoneId()).getZoneNm());
            if (usageHistMap.containsKey(zoneBookHist.getBookId()))
            {
                zoneBookHist.setCheckInDt(usageHistMap.get(zoneBookHist.getBookId()).getChkInDt());
                zoneBookHist.setCheckOutDt(usageHistMap.get(zoneBookHist.getBookId()).getChkOutDt());
                zoneBookHist.setAutoCheckOutYn(usageHistMap.get(zoneBookHist.getBookId()).getAutoChkOutYn());
            }

            this.setBookStatus(zoneBookHist);
            this.setMyBookInfo(zoneBookHist);
        }

        return result.stream().sorted(Comparator.comparing(ZoneBookHist::getBookStDt).reversed()).toList();
    }

    private void setMyBookInfo (ZoneUsageHist zoneUsageHist)
    {
        zoneUsageHist.setNickNm(SecurityUtil.getUserInfo().getNickNm());
    }

    private void setMyBookInfo (ZoneBookHist zoneBookHist)
    {
        zoneBookHist.setNickNm(SecurityUtil.getUserInfo().getNickNm());
        zoneBookHist.setIsMyBook(true);
    }

    private void setBookStatus (ZoneBookHist zoneBookHist)
    {
        // 상태값 세팅
        if (BooleanUtils.isTrue(zoneBookHist.getAutoBookCnclYn()) || BooleanUtils.isTrue(zoneBookHist.getBookCnclYn()))
        {
            zoneBookHist.setStatus(SwinglabConst.STATUS.CANCEL);
        } else if (zoneBookHist.getCheckOutDt() != null || zoneBookHist.getCheckInDt() != null)
        {
            zoneBookHist.setStatus(SwinglabConst.STATUS.USE);
        } else
        {
            zoneBookHist.setStatus(SwinglabConst.STATUS.WAITTING);
        }

        //        if (BooleanUtils.isTrue(zoneBookHist.getAutoBookCnclYn())) // 자동예약취소
        //        {
        //            zoneBookHist.setStatus(SwinglabConst.STATUS.AUTO_CANCEL);
        //        } else if (BooleanUtils.isTrue(zoneBookHist.getBookCnclYn())) // 예약취소
        //        {
        //            zoneBookHist.setStatus(SwinglabConst.STATUS.CANCEL);
        //        } else if (zoneBookHist.getCheckOutDt() != null) // 퇴실
        //        {
        //            zoneBookHist.setStatus(SwinglabConst.STATUS.CHECK_OUT);
        //        } else if (zoneBookHist.getCheckInDt() != null) // 입실
        //        {
        //            zoneBookHist.setStatus(SwinglabConst.STATUS.CHECK_IN);
        //        } else
        //        {
        //            zoneBookHist.setStatus(SwinglabConst.STATUS.BOOK);
        //        }
    }

}
