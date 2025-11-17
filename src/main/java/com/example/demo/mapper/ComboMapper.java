package com.example.demo.mapper;

import com.example.demo.dto.request.ComboCreationRequest;
import com.example.demo.dto.request.ComboUpdateRequest;
import com.example.demo.dto.response.ComboResponse;
import com.example.demo.entity.Combo;
import com.example.demo.entity.Combo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ComboMapper {
    Combo toCombo(ComboCreationRequest request);
    void updateCombo(@MappingTarget Combo combo, ComboUpdateRequest request);
    ComboResponse toComboResponse(Combo combo);
}
