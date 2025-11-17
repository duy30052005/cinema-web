package com.example.demo.mapper;

import com.example.demo.dto.request.BillComboCreationRequest;
import com.example.demo.dto.request.BillComboUpdateRequest;
import com.example.demo.dto.request.BillCreationRequest;
import com.example.demo.dto.request.BillUpdateRequest;
import com.example.demo.dto.response.BillComboResponse;
import com.example.demo.dto.response.BillResponse;
import com.example.demo.entity.Bill;
import com.example.demo.entity.BillCombo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BillComboMapper {
    BillCombo toBillCombo(BillComboCreationRequest request);
    void updateBillCombo(@MappingTarget BillCombo billCombo, BillComboUpdateRequest request);
    BillComboResponse toBillComboResponse(BillCombo billCombo);
}
