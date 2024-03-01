package com.dailystudy.swinglab.service.business.common.domain;

import com.dailystudy.swinglab.service.business.common.domain.entity.zone.ZoneBookHist;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookDayInfo
{
    private LocalDate bookDay;
    List<ZoneBookHist> bookList;
}
