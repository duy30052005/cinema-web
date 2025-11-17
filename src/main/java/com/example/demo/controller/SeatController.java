package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.service.SeatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/room/{room_id}")
    ApiResponse<List<SeatResponse>> getSeatByRoomId(@PathVariable("room_id") Long roomId) {
        return ApiResponse.<List<SeatResponse>>builder()
                .result(seatService.getSeatsByRoomId(roomId))
                .build();
    }
    @GetMapping("/{seatId}")
    ApiResponse<SeatResponse> getSeatById(@PathVariable("seatId") Long seatId) {
        return ApiResponse.<SeatResponse>builder()
                .result(seatService.getSeatById(seatId))
                .build();
    }
}
