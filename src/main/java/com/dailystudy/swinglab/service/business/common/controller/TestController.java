package com.dailystudy.swinglab.service.business.common.controller;

import com.dailystudy.swinglab.service.business.common.domain.entity.user.Ticket;
import com.dailystudy.swinglab.service.business.common.domain.entity.user.User;
import com.dailystudy.swinglab.service.business.common.repository.user.TicketRepository;
import com.dailystudy.swinglab.service.business.common.repository.user.UserQueryRepository;
import com.dailystudy.swinglab.service.business.common.repository.user.UserRepository;
import com.dailystudy.swinglab.service.framework.http.response.PlatformResponseBuilder;
import com.dailystudy.swinglab.service.framework.http.response.domain.SuccessResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController
{
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final TicketRepository ticketRepository;

    @GetMapping("/test/{id}")
    public ResponseEntity<SuccessResponse<Integer>> getTestId (@PathVariable("id") Integer id)
    {
        return PlatformResponseBuilder.build(id);
    }

    @GetMapping("/test/user")
    public ResponseEntity<SuccessResponse<List<User>>> getTestId (Pageable pageable)
    {
        return PlatformResponseBuilder.build(userRepository.findAll(pageable));
    }

    @Transactional
    @PostMapping("/test/user/{userId}/ticket")
    public ResponseEntity<SuccessResponse<Ticket>> postUserTicket (@PathVariable("userId") Long userId, @RequestBody Ticket ticket)
    {
        ticket.setUserId(userId);
        ticket.setSvcRegDay(LocalDate.now());
        ticket = ticketRepository.save(ticket);

        userQueryRepository.updateTicketIdByUserId(userId, ticket.getTicketId());
        return PlatformResponseBuilder.build(ticket);
    }
}
