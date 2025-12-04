package com.example.demo.service;

import com.example.demo.dto.request.RoomCreationRequest;
import com.example.demo.dto.request.RoomUpdateRequest;
import com.example.demo.dto.request.SeatCreationRequest;
import com.example.demo.dto.response.RoomResponse;
import com.example.demo.dto.response.SeatResponse;
import com.example.demo.entity.Cinemas;
import com.example.demo.entity.Room;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Type;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.CinemasRepository;
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
    RoomMapper roomMapper;
    RoomRepository roomRepository;
    SeatService seatService;
    SeatRepository seatRepository;
    CinemasRepository cinemasRepository;

    @Transactional
    public RoomResponse create(RoomCreationRequest request) {
        // --- BƯỚC 1: KIỂM TRA RẠP (LOGIC MỚI) ---

        // Tìm rạp theo ID gửi lên (Giả sử trong request có field cinemaId)
        Cinemas cinema = cinemasRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new RuntimeException("Rạp chiếu không tồn tại!")); // Nên dùng AppException

        // Kiểm tra trạng thái của Rạp
        if (!"ACTIVE".equals(cinema.getStatus())) {
            throw new RuntimeException("Rạp đang ngừng hoạt động, không thể tạo phòng!");
        }

        //BƯỚC 2: TẠO PHÒNG
        Room room = roomMapper.toRoom(request);

        // Liên kết Phòng với Rạp vừa tìm được
        room.setCinema(cinema);

        // Gán trạng thái mặc định cho Phòng
        room.setStatus("ACTIVE");

        room = roomRepository.save(room);
        Long roomId = room.getRoomId();

        //BƯỚC 3: TẠO GHẾ (LOGIC CŨ GIỮ NGUYÊN)
        int seatCount = request.getSeatCount();
        // (Lưu ý: Set createdSeats ở đây chỉ để logic, ko cần thiết nếu không dùng lại)

        for (int i = 1; i <= seatCount; i++) {
            SeatCreationRequest seatRequest = SeatCreationRequest.builder()
                    .seatStatus("Trống") // Hoặc "AVAILABLE" nếu chuẩn hóa
                    .roomId(roomId)
                    .build();

            // Gọi seatService để tạo ghế
            seatService.createSeat(seatRequest);
        }
        return roomMapper.toRoomResponse(room);
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

    public RoomResponse updateRequest(RoomUpdateRequest request, Long roomId) {
        // 1. Tìm phòng cũ
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        // 2. Map dữ liệu cơ bản (Tên, Số ghế...)
        roomMapper.updateRoom(room, request);

        // 3. Xử lý thay đổi Rạp (Nếu có)
        if (request.getCinemaId() != null) {
            Cinemas newCinema = cinemasRepository.findById(request.getCinemaId())
                    .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + request.getCinemaId()));

            // Kiểm tra trạng thái rạp mới (Option)
            if (!"ACTIVE".equals(newCinema.getStatus())) {
                throw new RuntimeException("Không thể chuyển sang rạp đang ngừng hoạt động!");
            }

            // Gán rạp mới cho phòng
            room.setCinema(newCinema);
        }

        // 4. Lưu xuống DB
        log.info("Update Room {} successful", room.getName());
        room = roomRepository.save(room);

        // 5. Tạo Response
        RoomResponse response = roomMapper.toRoomResponse(room);

        // 🔥 FIX LỖI: Gán thủ công tên rạp mới nhất vào Response
        // (Để đảm bảo dù Mapper có bị cache dữ liệu cũ thì dòng này vẫn ghi đè lại đúng)
        if (room.getCinema() != null) {
            response.setCinemaName(room.getCinema().getName());
        }

        return response;
    }
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
