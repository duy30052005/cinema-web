package com.example.demo.mapper;

import com.example.demo.dto.request.ShowtimeCreationRequest;
import com.example.demo.dto.request.ShowtimeUpdateRequest;
import com.example.demo.dto.response.ShowtimeResponse;
import com.example.demo.entity.Showtime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {
    Showtime toShowtime(ShowtimeCreationRequest request);
    void updateShowtime(@MappingTarget Showtime showtime, ShowtimeUpdateRequest request);
    ShowtimeResponse toShowtimeResponse(Showtime showtime);
}
