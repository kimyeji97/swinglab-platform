package com.dailystudy.swinglab.service.business.common.domain.entity.user;

import com.dailystudy.swinglab.service.framework.core.gen.entity.TicketCore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@NoArgsConstructor
@Table(name="tb_ticket")
@EqualsAndHashCode(callSuper = true)
public class Ticket extends TicketCore
{
}
