package com.example.demo.mapper;

import com.example.demo.dto.request.SeatCreationRequest;
import com.example.demo.dto.request.ShowtimeCreationRequest;
import com.example.demo.dto.request.ShowtimeUpdateRequest;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.dto.response.ShowtimeResponse;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Showtime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toSeat(SeatCreationRequest request);
    void updateSeat(@MappingTarget Seat seat, SeatCreationRequest request);
    SeatResponse toSeatResponse(Seat seat);
}
