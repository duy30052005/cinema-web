package com.example.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketCreationRequest {
    Long ticketId;

    Long userId;

    Long seatId;

    String ticketName;

    Long showtimeId;

    BigDecimal price;

    Long billId;


}
