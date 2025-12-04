package com.example.demo.controller;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.CinemaResponse;
import com.example.demo.dto.response.ComboResponse;
import com.example.demo.service.CinemaService;
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
@RequestMapping("/cinemas")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CinemaController {
    CinemaService cinemaService;

    @PostMapping
    ApiResponse<CinemaResponse> createCinema(@RequestBody @Valid CinemaCreationRequest request){
        return ApiResponse.<CinemaResponse>builder()
                .result(cinemaService.create(request))
                .build();
    }
    @PutMapping("/{cinemaId}")
    ApiResponse<CinemaResponse> updateCinema(@PathVariable("cinemaId") Long cinemaId, @RequestBody @Valid CinemaUpdateRequest request){
        return ApiResponse.<CinemaResponse>builder()
                .result(cinemaService.updateRequest(request,cinemaId))
                .build();
    }
    @GetMapping
    ApiResponse<List<CinemaResponse>> getAllCinemas(){
        return ApiResponse.<List<CinemaResponse>>builder()
                .result(cinemaService.getAllCinema())
                .build();
    }
//    @GetMapping("/{comboId}")
//    ApiResponse<ComboResponse> getCombo(@PathVariable Long comboId){
//        return ApiResponse.<ComboResponse>builder()
//                .result(comboService.getComboById(comboId))
//                .build();
//    }
    @DeleteMapping("/{cinemaId}")
    ApiResponse<Void> deleteCinema(@PathVariable Long cinemaId){
        cinemaService.deleteCinema(cinemaId);
        return ApiResponse.<Void>builder()
                .build();
    }

}
