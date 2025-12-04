package com.example.demo.mapper;

import com.example.demo.dto.request.CinemaCreationRequest;
import com.example.demo.dto.request.CinemaUpdateRequest;
import com.example.demo.dto.request.ComboUpdateRequest;
import com.example.demo.dto.response.CinemaResponse;
import com.example.demo.dto.response.ComboResponse;
import com.example.demo.entity.Cinemas;
import com.example.demo.entity.Combo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CinemasMapper {
    Cinemas toCinema(CinemaCreationRequest request);
    CinemaResponse toCinemaResponse(Cinemas cinema);
    void updateCinema(@MappingTarget Cinemas cinema, CinemaUpdateRequest request);

}
