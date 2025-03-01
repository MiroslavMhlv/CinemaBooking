package com.softuniproject.app.ticket.service;

import com.softuniproject.app.exception.DomainException;
import com.softuniproject.app.movie.entity.Movie;
import com.softuniproject.app.ticket.entity.Ticket;
import com.softuniproject.app.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    // Връщане на всички билети (с кеширане)
    @Cacheable(value = "tickets")
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Връщане на билет по ID
    public Ticket getTicketById(UUID id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new DomainException("Ticket with id [%s] does not exist.".formatted(id)));
    }

    // Връщане на билети за даден филм
    public List<Ticket> getTicketsByMovie(Movie movie) {
        return ticketRepository.findByMovieId(movie.getId());
    }

    // Добавяне на нов билет
    @CacheEvict(value = "tickets", allEntries = true)
    public Ticket addTicket(Ticket ticket) {
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket [{}] added successfully.", savedTicket.getId());
        return savedTicket;
    }

    // Изтриване на билет
    @CacheEvict(value = "tickets", allEntries = true)
    public void deleteTicket(UUID id) {
        if (!ticketRepository.existsById(id)) {
            throw new DomainException("Ticket with id [%s] does not exist.".formatted(id));
        }
        ticketRepository.deleteById(id);
        log.info("Ticket with id [{}] deleted successfully.", id);
    }
}
