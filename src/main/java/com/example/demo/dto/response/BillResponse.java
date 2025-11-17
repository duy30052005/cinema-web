package com.example.demo.dto.response;

import com.example.demo.entity.BillCombo;
import com.example.demo.entity.Ticket;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillResponse {
    Long billId;

    Long userId;

    BigDecimal totalAmount;

    String paymentMethod;

    String paymentStatus;

    List<TicketResponse> ticketIds;

    List<BillComboResponse> billComboIds;

    LocalDateTime createdAt;

    LocalDateTime paymentAt;
}
