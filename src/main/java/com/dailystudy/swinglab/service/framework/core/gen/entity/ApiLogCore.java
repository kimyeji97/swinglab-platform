package com.dailystudy.swinglab.service.framework.core.gen.entity;

import com.dailystudy.swinglab.service.framework.core.BaseEntity;
import jakarta.persistence.EntityListeners;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;


@Data
@MappedSuperclass // 테이블로 생성되지 않도록 해주는 어노테이션
@EntityListeners(value = {AuditingEntityListener.class}) // AuditingEntityListener : JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는 역할
@EqualsAndHashCode(callSuper = true)
public class ApiLogCore extends BaseEntity
{
    @Id
    @Column(name = "tx_id", nullable = false)
    private String txId;
    @Column(name = "req_dt", nullable = false)
    private Date reqDt;
    @Column(name = "clint_ip", nullable = false)
    private String clintIp;
    @Column(name = "api", nullable = false)
    private String api;
    @Column(name = "req_body")
    private String reqBody;
    @Column(name = "res_dt")
    private Date resDt;
    @Column(name = "res_yn")
    private Boolean resYn;
    @Column(name = "http_sttus_cd")
    private Integer httpSttusCd;
    @Column(name = "res_cd")
    private String resCd;
    @Column(name = "res_body")
    private String resBody;
    @Column(name = "res_body_len")
    private Long resBodyLen;

}