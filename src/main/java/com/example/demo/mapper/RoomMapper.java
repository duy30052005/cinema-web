package com.example.demo.mapper;

import com.example.demo.dto.request.MovieCreationRequest;
import com.example.demo.dto.request.MovieUpdateRequest;
import com.example.demo.dto.request.RoomCreationRequest;
import com.example.demo.dto.request.RoomUpdateRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "cinema", ignore = true)
    Room toRoom(RoomCreationRequest request);

    void updateRoom(@MappingTarget Room room, RoomUpdateRequest request);

    @Mapping(source = "cinema.cinemaId", target = "cinemaId") // ✅ THÊM DÒNG NÀY
    @Mapping(source = "cinema.name", target = "CinemaName")
    RoomResponse toRoomResponse(Room room);
}
