package com.example.demo.service;

import com.example.demo.dto.request.RoomCreationRequest;
import com.example.demo.dto.request.RoomUpdateRequest;
import com.example.demo.dto.request.SeatCreationRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Type;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.SeatRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RoomService {
    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final SeatService seatService;
    private final SeatRepository seatRepository;

    @Transactional
    public RoomResponse create(RoomCreationRequest request) {
        Room room = roomMapper.toRoom(request);
        room = roomRepository.save(room);
        Long roomId = room.getRoomId();

        int seatCount = request.getSeatCount();
        Set<Seat> createdSeats = new HashSet<>();
        for (int i = 1; i <= seatCount; i++) {
            SeatCreationRequest seatRequest = SeatCreationRequest.builder()
                    .seatStatus("Trống")
                    .roomId(roomId)
                    .build();
            SeatResponse seatResponse = seatService.createSeat(seatRequest);
            Seat seat = seatRepository.findById(seatResponse.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found after creation"));
            createdSeats.add(seat);
        }

        room.setSeatCount(seatCount);
        room = roomRepository.save(room);

        RoomResponse roomResponse = roomMapper.toRoomResponse(room);
        // Không cần gán seatResponses nữa vì RoomResponse không có trường seats
        return roomResponse;
    }
    public List<RoomResponse> getAllRoom() {
        log.info("Get all Room");
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(room -> {
                    log.info("Processing room with id: {}", room.getRoomId());
                    RoomResponse roomResponse = roomMapper.toRoomResponse(room);
                    // Loại bỏ việc lấy và gán seatResponses
                    return roomResponse;
                })
                .collect(Collectors.toList());
    }

    public RoomResponse getRoomById(Long room_id) {
        log.info("Get Room by title: {}", room_id);
        Room room = roomRepository.findById(room_id)
                .orElseThrow(() -> new AppException(ErrolCode.ROOM_NOT_FOUND));

        RoomResponse roomResponse = roomMapper.toRoomResponse(room);
        // Loại bỏ việc lấy và gán seatResponses
        return roomResponse;
    }

    public RoomResponse updateRequest(RoomUpdateRequest request, long room_id) {
        Room room = roomRepository.findById(room_id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomMapper.updateRoom(room, request);

        log.info("Update Room {} successful", room.getName());
        room = roomRepository.save(room);

        RoomResponse roomResponse = roomMapper.toRoomResponse(room);
        // Loại bỏ việc lấy và gán seatResponses
        return roomResponse;
    }
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
