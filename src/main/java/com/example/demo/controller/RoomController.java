package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.RoomCreationRequest;
import com.example.demo.dto.request.RoomUpdateRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.entity.Room;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.RoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @PostMapping
    ApiResponse<RoomResponse> createRoom(@RequestBody @Valid RoomCreationRequest request) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoomResponse>> getAllRooms() {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAllRoom())
                .build();
    }
    @GetMapping("/{room_id}")
    ApiResponse<RoomResponse> getRoomById(@PathVariable("room_id") Long roomId) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.getRoomById(roomId))
                .build();
    }
    @PutMapping("/{room_id}")
    ApiResponse<RoomResponse> updateRoom(@RequestBody @Valid RoomUpdateRequest request,@PathVariable("room_id") Long roomId) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.updateRequest(request, roomId))
                .build();
    }
    @DeleteMapping("/{room_id}")
    ApiResponse<Void> deleteRoom(@PathVariable("room_id") Long roomId) {
        roomService.deleteRoom(roomId);
        return ApiResponse.<Void>builder().build();
    }
}
