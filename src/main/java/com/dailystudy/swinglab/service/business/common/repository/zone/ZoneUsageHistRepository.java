package com.dailystudy.swinglab.service.business.common.repository.zone;

import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneUsageHist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneUsageHistRepository extends JpaRepository<ZoneUsageHist, Long>
{
}
