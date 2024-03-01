package com.dailystudy.swinglab.service.framework.core.gen.entity;

import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@MappedSuperclass // 테이블로 생성되지 않도록 해주는 어노테이션
@EqualsAndHashCode(callSuper = true)
@EntityListeners(value = {AuditingEntityListener.class}) // AuditingEntityListener : JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는 역할
public class ZoneBookHistCore extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID", nullable = false)
    private Long bookId;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "ZONE_ID", nullable = false)
    private Long zoneId;
    @JsonProperty(value = "start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DT_FORMAT)
    @Column(name = "BOOK_ST_DT", nullable = false)
    private LocalDateTime bookStDt;
    @JsonProperty(value = "end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DT_FORMAT)
    @Column(name = "BOOK_ED_DT", nullable = false)
    private LocalDateTime bookEdDt;
    @Column(name = "BOOK_CNCL_YN", nullable = false)
    private Boolean bookCnclYn;
    @Column(name = "AUTO_BOOK_CNCL_YN", nullable = false)
    private Boolean autoBookCnclYn;
}