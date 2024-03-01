package com.dailystudy.swinglab.service.framework.core.gen.entity;

import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@MappedSuperclass // 테이블로 생성되지 않도록 해주는 어노테이션
@EntityListeners(value = {AuditingEntityListener.class}) // AuditingEntityListener : JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는 역할
@EqualsAndHashCode(callSuper = true)
public class TicketCore extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DAY_FORMAT, timezone = SwinglabConst.TIME_ZONE)
    @Column(name = "SVC_REG_DAY")
    private LocalDate svcRegDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DAY_FORMAT, timezone = SwinglabConst.TIME_ZONE)
    @Column(name = "SVC_ST_DAY")
    private LocalDate svcStDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DAY_FORMAT, timezone = SwinglabConst.TIME_ZONE)
    @Column(name = "SVC_ED_DAY")
    private LocalDate svcEdDay;
}
