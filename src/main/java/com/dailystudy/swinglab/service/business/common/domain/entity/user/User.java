package com.dailystudy.swinglab.service.business.common.domain.entity.user;

import com.dailystudy.swinglab.service.framework.core.gen.entity.UserCore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name="tb_user")
@EqualsAndHashCode(callSuper = true)
public class User extends UserCore
{
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwdChk;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String crntPwd;
}
