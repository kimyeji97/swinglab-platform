package com.dailystudy.swinglab.service.business.common.domain.entity.zone;

import com.dailystudy.swinglab.service.framework.core.gen.entity.ZoneUsageHistCore;
import jakarta.persistence.Transient;
import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tb_zone_usage_hist")
@EqualsAndHashCode(callSuper = true)
public class ZoneUsageHist extends ZoneUsageHistCore
{
    @Transient
    private String nickNm;
}
