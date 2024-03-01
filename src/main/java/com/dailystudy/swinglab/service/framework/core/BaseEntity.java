package com.dailystudy.swinglab.service.framework.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity
{
    @JsonIgnore
    @CreatedDate
    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "upd_Dt")
    private LocalDateTime updDt;
}
