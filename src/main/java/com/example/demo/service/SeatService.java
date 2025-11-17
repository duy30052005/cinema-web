package com.example.demo.service;

import com.example.demo.dto.request.SeatCreationRequest;
import com.example.demo.dto.request.TypeCreationRequest;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.dto.response.TypeResponse;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Type;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.SeatMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.SeatRepository;
import com.example.demo.repository.TypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SeatService {
    TypeRepository typeRepository;
    TypeMapper typeMapper;
    SeatMapper seatMapper;
    SeatRepository seatRepository;
    RoomRepository roomRepository;

    public SeatResponse createSeat(SeatCreationRequest request){
        Seat seat = seatMapper.toSeat(request);
        Room room = roomRepository.findById(request.getRoomId()).orElse(null);

        try {
            seat.setRoom(room);
            seat = seatRepository.save(seat);
            log.info("Saved seat: {}", seat);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return seatMapper.toSeatResponse(seat);
    }

    public List<SeatResponse> getSeatsByRoomId(Long roomId){
        List<Seat> seats= seatRepository.findByRoomRoomId(roomId);
        return seats.stream()
                .map(seatMapper::toSeatResponse)
                .collect(Collectors.toList());
    }
    public SeatResponse getSeatById(Long seatId) {
        log.info("Fetching seat with seatId: {}", seatId);
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new AppException(ErrolCode.SEAT_NOT_FOUND));
        return seatMapper.toSeatResponse(seat);
    }
//

}
