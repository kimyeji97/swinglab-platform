package com.dailystudy.swinglab.service.business.common.repository.zone;

import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneBookHist;
import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.dailystudy.swinglab.service.business.common.domain.entity.zone.QZoneBookHist.zoneBookHist;

@Repository
@RequiredArgsConstructor
public class ZoneBookHistQueryRepository
{
    private final JPAQueryFactory jpaQueryFactory;

    private BooleanBuilder getWhereBuilder (ZoneBookHist param)
    {
        BooleanBuilder builder = new BooleanBuilder();
        if (param.getBookId() != null)
        {
            builder.and(zoneBookHist.bookId.eq(param.getBookId()));
        }
        if (param.getUserId() != null)
        {
            builder.and(zoneBookHist.userId.eq(param.getUserId()));
        }
        if (param.getZoneId() != null)
        {
            builder.and(zoneBookHist.zoneId.eq(param.getZoneId()));
        }
        if (param.getBookCnclYn() != null)
        {
            builder.and(zoneBookHist.bookCnclYn.eq(param.getBookCnclYn()));
        }
        if (param.getAutoBookCnclYn() != null)
        {
            builder.and(zoneBookHist.autoBookCnclYn.eq(param.getAutoBookCnclYn()));
        }
        if (param.getBookDaySt() != null)
        {
            builder.and(zoneBookHist.bookEdDt.goe(param.getBookDaySt().atStartOfDay()));
        }
        if (param.getBookDayEd() != null)
        {
            builder.and(zoneBookHist.bookStDt.loe(param.getBookDayEd().atTime(23, 59, 59, 59)));
        }
        return builder;
    }

    /**
     * pk로 단건 조회
     *
     * @param bookId
     * @return
     */
    public ZoneBookHist findOneByKey (Long bookId)
    {
        return jpaQueryFactory
                .selectFrom(zoneBookHist)
                .where(zoneBookHist.bookId.eq(bookId))
                .fetchOne();
    }

    /**
     * list 조회
     *
     * @param param
     * @return
     */
    public List<ZoneBookHist> findAllByWhere (ZoneBookHist param)
    {
        return jpaQueryFactory
                .selectFrom(zoneBookHist)
                .where(this.getWhereBuilder(param))
                .fetch();
    }

    /**
     * 타석 아이디와 예약일시 조건으로 리스트 조회
     *
     * @param zoneId
     * @param bookStDt
     * @param bookEdDt
     * @return
     */
    public List<ZoneBookHist> findAllByZoneIdAndBookDt (Long zoneId, LocalDateTime bookStDt, LocalDateTime bookEdDt)
    {

        return jpaQueryFactory
                .selectFrom(zoneBookHist)
                .where(zoneBookHist.zoneId.eq(zoneId)
                        , zoneBookHist.bookCnclYn.isFalse()
                        , zoneBookHist.bookEdDt.goe(bookStDt)
                        , zoneBookHist.bookStDt.loe(bookEdDt)
                ).fetch();
    }

    /**
     * 해당 유저의 다음 예약 1건 조회
     *
     * @param userId
     * @return
     */
    public ZoneBookHist findNextOneByUserId (Long userId)
    {
        return jpaQueryFactory
                .selectFrom(zoneBookHist)
                .where(zoneBookHist.userId.eq(userId)
                        , zoneBookHist.bookCnclYn.isFalse()
                        , zoneBookHist.bookStDt.goe(LocalDateTime.now().minusMinutes(SwinglabConst.DF_MIN))
                )
                .orderBy(zoneBookHist.bookStDt.asc())
                .fetchFirst();
    }

    /**
     * 해당 예약 취소 처리
     *
     * @param bookId
     */
    public void updateBookCnclYnTrueByKey (Long bookId)
    {
        jpaQueryFactory
                .update(zoneBookHist)
                .set(zoneBookHist.bookCnclYn, true)
                .set(zoneBookHist.updDt, LocalDateTime.now())
                .where(zoneBookHist.bookId.eq(bookId))
                .execute();
    }

}
