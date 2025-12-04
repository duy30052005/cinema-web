package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.CinemaResponse;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Cinemas;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import com.example.demo.enums.Status;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.CinemasMapper;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.repository.CinemasRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.SeatRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CinemaService {
    CinemasMapper cinemaMapper;
    CinemasRepository cinemaRepository;
    SeatService seatService;
    SeatRepository seatRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CinemaResponse create(CinemaCreationRequest request) {
        Cinemas cinema = cinemaMapper.toCinema(request);
        cinema.setStatus("ACTIVE");
        cinema = cinemaRepository.save(cinema);
        Long cinemaId = cinema.getCinemaId();


        CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
        return cinemaResponse;
    }
    public List<CinemaResponse>     getAllCinema() {
        log.info("Get all Cinema");
        List<Cinemas> cinemas = cinemaRepository.findAll();

        return cinemas.stream()
                .map(cinema -> {
                    log.info("Processing room with id: {}", cinema.getCinemaId());
                    CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
                    // Loại bỏ việc lấy và gán seatResponses
                    return cinemaResponse;
                })
                .collect(Collectors.toList());
    }
//
//    public RoomResponse getRoomById(Long room_id) {
//        log.info("Get Room by title: {}", room_id);
//        Room room = roomRepository.findById(room_id)
//                .orElseThrow(() -> new AppException(ErrolCode.ROOM_NOT_FOUND));
//
//        RoomResponse roomResponse = roomMapper.toRoomResponse(room);
//        // Loại bỏ việc lấy và gán seatResponses
//        return roomResponse;
//    }
//
    @PreAuthorize("hasRole('ADMIN')")
    public CinemaResponse updateRequest(CinemaUpdateRequest request, long cinema_id) {
        Cinemas cinema = cinemaRepository.findById(cinema_id)
                .orElseThrow(() -> new RuntimeException("Cinema not found"));
        cinemaMapper.updateCinema(cinema, request);

        log.info("Update Cinema {} successful", cinema.getName());
        cinema = cinemaRepository.save(cinema);

        CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
        // Loại bỏ việc lấy và gán seatResponses
        return cinemaResponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCinema(Long cinemaId) {
        cinemaRepository.deleteById(cinemaId);
    }
}
