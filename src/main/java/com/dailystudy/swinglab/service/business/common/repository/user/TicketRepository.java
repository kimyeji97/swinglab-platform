package com.dailystudy.swinglab.service.business.common.repository.user;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>
{
}
