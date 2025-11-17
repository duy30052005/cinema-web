package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.BillCreationRequest;
import com.example.demo.dto.request.BillUpdateRequest;
import com.example.demo.dto.response.BillResponse;
import com.example.demo.service.BillService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillController {
    BillService billService;

    @PostMapping
    ApiResponse<BillResponse> createBill(@RequestBody @Valid BillCreationRequest request) {
        log.info("Create bill request: {}", request);
        return ApiResponse.<BillResponse>builder()
                .result(billService.createBill(request))
                .build();
    }
    @PutMapping("/{billId}")
    ApiResponse<BillResponse> updateBill(@PathVariable("billId") Long billId,@RequestBody @Valid BillUpdateRequest request) {
        log.info("Update bill request: {}", billId);
        return ApiResponse.<BillResponse>builder()
                .result(billService.updateBill(billId,request))
                .build();
    }
    @GetMapping
    ApiResponse<List<BillResponse>> getAllBills() {
        log.info("Get all bills");
        return ApiResponse.<List<BillResponse>>builder()
                .result(billService.getAllBills())
                .build();
    }
    @GetMapping("/{billId}")
    ApiResponse<BillResponse> getBillById(@PathVariable("billId") Long billId) {
        log.info("Get bill by id: {}", billId);
        return ApiResponse.<BillResponse>builder()
                .result(billService.getBillById(billId))
                .build();
    }
}
