package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.ShowtimeCreationRequest;
import com.example.demo.dto.request.ShowtimeUpdateRequest;
import com.example.demo.dto.response.ShowtimeResponse;
import com.example.demo.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ShowtimeController {
    ShowtimeService showtimeService;
    @PostMapping
    ApiResponse<ShowtimeResponse> create(@RequestBody @Valid ShowtimeCreationRequest request){
        return ApiResponse.<ShowtimeResponse>builder()
                .result(showtimeService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<ShowtimeResponse>> getAllShowtimes(){
        return  ApiResponse.<List<ShowtimeResponse>>builder()
                .result(showtimeService.getAllShowtime())
                .build();
    }
    @GetMapping("/room/{roomId}")
    ApiResponse<List<ShowtimeResponse>> getShowtimesByRoomId(@PathVariable("roomId") Long roomId){
        return ApiResponse.<List<ShowtimeResponse>>builder()
                .result(showtimeService.getShowtimeByRoom(roomId))
                .build();
    }
    @GetMapping("/movie/{movieId}")
    ApiResponse<List<ShowtimeResponse>> getShowtimesByMovieId(@PathVariable("movieId") Long movieId){
        return ApiResponse.<List<ShowtimeResponse>>builder()
                .result(showtimeService.getShowtimebyMovie(movieId))
                .build();
    }
    @GetMapping("/{showtimeId}")
    ApiResponse<ShowtimeResponse> getShowtimeByShowtimeId(@PathVariable("showtimeId") Long showtimeId){
        return ApiResponse.<ShowtimeResponse>builder()
                .result(showtimeService.getShowtimeById(showtimeId))
                .build();
    }
    @PutMapping("/{showtimeId}")
    ApiResponse<ShowtimeResponse> updateShowtime(@PathVariable("showtimeId") Long showtimeId, @RequestBody ShowtimeUpdateRequest request){
        return ApiResponse.<ShowtimeResponse>builder()
                .result(showtimeService.updateShowtime(showtimeId,request))
                .build();
    }
    @DeleteMapping("/{showtimeId}")
    ApiResponse<Void> deleteShowtime(@PathVariable("showtimeId") Long showtimeId){
        showtimeService.deleteShowtime(showtimeId);
        return ApiResponse.<Void>builder().build();
    }
}
