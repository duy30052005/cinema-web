package com.example.demo.mapper;

import com.example.demo.dto.request.MovieCreationRequest;
import com.example.demo.dto.request.MovieUpdateRequest;
import com.example.demo.dto.request.TicketCreationRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.dto.response.TicketResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toTicket(TicketCreationRequest request);
//    void updateTicket(@MappingTarget Ticket ticket, TicketUpdateRequest request);
    TicketResponse toTicketResponse(Ticket ticket);
}
