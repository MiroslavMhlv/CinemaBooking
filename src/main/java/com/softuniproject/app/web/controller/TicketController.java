package com.softuniproject.app.web.controller;

import com.softuniproject.app.ticket.entity.Ticket;
import com.softuniproject.app.ticket.service.TicketService;
import com.softuniproject.app.web.dto.TicketRequest;
import com.softuniproject.app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final DtoMapper dtoMapper;

    // 🔹 Преглед на всички билети
    @GetMapping
    public ModelAndView getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        ModelAndView modelAndView = new ModelAndView("tickets");
        modelAndView.addObject("tickets", tickets);
        return modelAndView;
    }

    // 🔹 Зареждане на формата за добавяне на нов билет
    @GetMapping("/add")
    public ModelAndView getAddTicketForm() {
        ModelAndView modelAndView = new ModelAndView("add-ticket");
        modelAndView.addObject("ticketRequest", new TicketRequest());
        return modelAndView;
    }

    // 🔹 Добавяне на нов билет
    @PostMapping("/add")
    public ModelAndView addTicket(@Valid TicketRequest ticketRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("add-ticket");
        }

        ticketService.addTicket(dtoMapper.mapTicketRequestToTicket(ticketRequest));
        return new ModelAndView("redirect:/tickets");
    }

    // 🔹 Изтриване на билет
    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return "redirect:/tickets";
    }
}
