package com.dailystudy.swinglab.service.business.common.domain.entity.zone;

import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.core.gen.entity.ZoneBookHistCore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tb_zone_book_hist")
@EqualsAndHashCode(callSuper = true)
public class ZoneBookHist extends ZoneBookHistCore
{
    @Transient
    private SwinglabConst.STATUS status;
    @Transient
    private String statusNm;
    @Transient
    private String zoneNm; // 타석 명
    @Transient
    private Boolean isMyBook; // 내 예약
    @Transient
    private String nickNm; // 닉네임


    @Transient
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DT_FORMAT)
    private LocalDateTime checkInDt;
    @Transient
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DT_FORMAT)
    private LocalDateTime checkOutDt;
    @Transient
    private Boolean autoCheckOutYn;


    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DAY_FORMAT)
    private LocalDate bookDaySt;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DAY_FORMAT)
    private LocalDate bookDayEd;

    public void setStatus(SwinglabConst.STATUS status)
    {
        this.status = status;
        this.statusNm = status.getStatus();
    }
}


