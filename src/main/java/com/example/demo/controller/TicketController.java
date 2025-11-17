package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.TicketCreationRequest;
import com.example.demo.dto.response.TicketResponse;
import com.example.demo.service.TicketService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    ApiResponse<TicketResponse> createTicket(@RequestBody @Valid TicketCreationRequest request){
        log.info("Create ticket request: {}", request);
        return ApiResponse.<TicketResponse>builder()
                .result(ticketService.create(request))
                .build();
    }
    @GetMapping("showtime/{showtimeId}")
    ApiResponse<List<TicketResponse>> getTicketbyShowtime(@PathVariable("showtimeId") Long showtimeId){
        return ApiResponse.<List<TicketResponse>>builder()
                .result((ticketService.getTicketByShowtime(showtimeId)))
                .build();
    }

    @GetMapping("bill/{billId}")
    ApiResponse<List<TicketResponse>> getTicketbyBill(@PathVariable("billId") Long billId){
        return ApiResponse.<List<TicketResponse>>builder()
                .result(ticketService.getTicketByBill(billId))
                .build();
    }
}
