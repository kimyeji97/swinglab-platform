package com.dailystudy.swinglab.service.framework.core.gen.entity;

import com.dailystudy.swinglab.service.framework.core.BaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

@MappedSuperclass // 테이블로 생성되지 않도록 해주는 어노테이션
@EntityListeners(value = {AuditingEntityListener.class}) // AuditingEntityListener : JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는 역할
@Data
@EqualsAndHashCode(callSuper = true)
public class ZoneCore extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ZONE_ID", nullable = false)
    private Long zoneId;
    @Column(name = "ZONE_NM", nullable = false)
    private String zoneNm;
}