package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.BillComboCreationRequest;
import com.example.demo.dto.response.BillComboResponse;
import com.example.demo.service.BillComboService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bill-combos")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillComboController {
    BillComboService billComboService;

    @PostMapping
    ApiResponse<BillComboResponse> createBillCombo(@RequestBody @Valid BillComboCreationRequest request) {
        log.info("Create bill combo request: {}", request);
        return ApiResponse.<BillComboResponse>builder()
                .result(billComboService.createBillCombo(request))
                .build();
    }
    @GetMapping("/bill/{billId}")
    ApiResponse<List<BillComboResponse>> getBillComboByBill(@PathVariable("billId") Long billId) {
        log.info("Get bill combo by id: {}", billId);
        return ApiResponse.<List<BillComboResponse>>builder()
                .result(billComboService.getBillCombosByBillId(billId))
                .build();
    }
}
