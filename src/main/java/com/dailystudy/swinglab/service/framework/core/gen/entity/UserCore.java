package com.dailystudy.swinglab.service.framework.core.gen.entity;

import com.dailystudy.swinglab.service.framework.core.SwinglabConst;
import com.dailystudy.swinglab.service.framework.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@MappedSuperclass // 테이블로 생성되지 않도록 해주는 어노테이션
@EntityListeners(value = {AuditingEntityListener.class}) // AuditingEntityListener : JPA 내부에서 엔티티 객체가 생성/변경 되는것을 감지하는 역할
@EqualsAndHashCode(callSuper = true)
public class UserCore extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "LOGIN_ID", nullable = false)
    private String loginId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "PWD", nullable = false)
    private String pwd;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "NICK_NM")
    private String nickNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SwinglabConst.DT_FORMAT, timezone = SwinglabConst.TIME_ZONE)
    @Column(name = "SIGNUP_DT")
    private LocalDateTime signupDt;
    @JsonIgnore
    @Column(name = "DEL_YN", nullable = false)
    private Boolean delYn;
    @JsonIgnore
    @Column(name = "TICKET_ID")
    private Long ticketId;
}