package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.ComboCreationRequest;
import com.example.demo.dto.request.ComboUpdateRequest;
import com.example.demo.dto.response.ComboResponse;
import com.example.demo.service.ComboService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/combos")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ComboController {
    ComboService comboService;

    @PostMapping
    ApiResponse<ComboResponse> createCombo(@RequestBody @Valid ComboCreationRequest request){
        return ApiResponse.<ComboResponse>builder()
                .result(comboService.createCombo(request))
                .build();
    }
    @PutMapping("/{comboId}")
    ApiResponse<ComboResponse> updateCombo(@PathVariable("comboId") Long comboId, @RequestBody @Valid ComboUpdateRequest request){
        return ApiResponse.<ComboResponse>builder()
                .result(comboService.updateCombo(request,comboId))
                .build();
    }
    @GetMapping
    ApiResponse<List<ComboResponse>> getAllCombos(){
        return ApiResponse.<List<ComboResponse>>builder()
                .result(comboService.getAllCombos())
                .build();
    }
    @GetMapping("/{comboId}")
    ApiResponse<ComboResponse> getCombo(@PathVariable Long comboId){
        return ApiResponse.<ComboResponse>builder()
                .result(comboService.getComboById(comboId))
                .build();
    }
    @DeleteMapping("/{comboId}")
    ApiResponse<Void> deleteCombo(@PathVariable Long comboId){
        comboService.deleteComboById(comboId);
        return ApiResponse.<Void>builder()
                .build();
    }

}
