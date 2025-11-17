package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillComboResponse {
    Long billComboId;

    Long billId;

    Long comboId;

    Long quantity;

    BigDecimal totalPrice;
}
