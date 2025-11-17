package com.example.demo.dto.response;


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
public class TicketResponse {
    Long ticketId;

    Long userId;

    Long seatId;

    Long showtimeId;

    LocalDateTime bookingDate;

    String ticketName;

    BigDecimal price;

    Long billId;

}
