package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaResponse {
    Long cinemaId;

    String name;

    String address;

    String status;
//    List<TicketResponse> ticketIds;
//
//    List<BillComboResponse> billComboIds;
//
//    LocalDateTime createdAt;
//
//    LocalDateTime paymentAt;
}
