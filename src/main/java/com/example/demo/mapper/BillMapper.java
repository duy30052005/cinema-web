package com.example.demo.mapper;

import com.example.demo.dto.request.BillCreationRequest;
import com.example.demo.dto.request.BillUpdateRequest;
import com.example.demo.dto.response.BillResponse;
import com.example.demo.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BillMapper {
    Bill toBill(BillCreationRequest request);
    void updateBill(@MappingTarget Bill bill, BillUpdateRequest request);
    BillResponse toBillResponse(Bill bill);
}
