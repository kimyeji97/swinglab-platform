package com.dailystudy.swinglab.service.business.common.domain.entity.common;

import com.dailystudy.swinglab.service.framework.core.gen.entity.ApiLogCore;
import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@Table(name="tb_api_log")
@EqualsAndHashCode(callSuper = true)
public class ApiLog extends ApiLogCore
{
}



