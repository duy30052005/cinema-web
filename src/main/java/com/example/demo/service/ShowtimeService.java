package com.example.demo.service;

import com.example.demo.dto.request.ShowtimeCreationRequest;
import com.example.demo.dto.request.ShowtimeUpdateRequest;
import com.example.demo.dto.response.ShowtimeResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Room;
import com.example.demo.entity.Showtime;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.ShowtimeMapper;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.ShowtimeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowtimeService {
    ShowtimeMapper showtimeMapper;
    ShowtimeRepository showtimeRepository;
    MovieRepository movieRepository;
    RoomRepository roomRepository;

    // Helper method để map Showtime sang ShowtimeResponse và điền các thông tin phụ (CinemaName, RoomName, MovieTitle)
    private ShowtimeResponse mapToResponse(Showtime showtime) {
        ShowtimeResponse response = showtimeMapper.toShowtimeResponse(showtime);

        // Map tên phim
        if (showtime.getMovie() != null) {
            response.setTitle(showtime.getMovie().getTitle());
        }

        // Map thông tin phòng và rạp
        if (showtime.getRoom() != null) {
            response.setName(showtime.getRoom().getName());
            response.setRoomId(showtime.getRoom().getRoomId());

            // Lấy tên rạp từ phòng
            if (showtime.getRoom().getCinema() != null) {
                response.setCinemaName(showtime.getRoom().getCinema().getName());
            }
        }

        return response;
    }

    @Transactional
    public ShowtimeResponse create(ShowtimeCreationRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new AppException(ErrolCode.MOVIE_NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrolCode.ROOM_NOT_FOUND));

        // --- CẬP NHẬT MỚI: Kiểm tra trạng thái phòng ---
        // Chỉ cho phép tạo suất chiếu nếu phòng đang ACTIVE
        if (!"ACTIVE".equalsIgnoreCase(room.getStatus())) {
            // Bạn có thể tạo thêm ErrolCode.ROOM_NOT_ACTIVE để quản lý lỗi tốt hơn
            throw new RuntimeException("Không thể tạo suất chiếu cho phòng đang bảo trì hoặc ngưng hoạt động");
        }
        // ------------------------------------------------

        // Tạo Showtime
        Showtime showtime = showtimeMapper.toShowtime(request);
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStatus("ACTIVE");

        // Tính endTime
        LocalTime endTime = showtime.getStartTime().plusMinutes(movie.getDuration());
        showtime.setEndTime(endTime);

        // Kiểm tra chồng lấn thời gian
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtime(
                request.getRoomId(),
                request.getShowDate(),
                showtime.getStartTime(),
                endTime
        );
        if (!overlappingShowtimes.isEmpty()) {
            throw new AppException(ErrolCode.SHOWTIME_OVERLAP);
        }

        // Lưu Showtime
        showtime = showtimeRepository.save(showtime);

        return mapToResponse(showtime);
    }

    @Transactional(readOnly = true)
    public List<ShowtimeResponse> getAllShowtime() {
        List<Showtime> showtimes = showtimeRepository.findAll();
        return showtimes.stream()
                .map(this::mapToResponse) // Sử dụng helper method
                .sorted(Comparator
                        .comparing(ShowtimeResponse::getShowDate)
                        .thenComparing(ShowtimeResponse::getStartTime))
                .collect(Collectors.toList());
    }

    public List<ShowtimeResponse> getShowtimeByRoom(Long roomId) {
        List<Showtime> showtimes = showtimeRepository.findByRoomRoomId(roomId);
        return showtimes.stream()
                .map(this::mapToResponse)
                .sorted(Comparator
                        .comparing(ShowtimeResponse::getShowDate)
                        .thenComparing(ShowtimeResponse::getStartTime))
                .collect(Collectors.toList());
    }

    public List<ShowtimeResponse> getShowtimebyMovie(Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByMovieMovieId(movieId);
        return showtimes.stream()
                .map(this::mapToResponse)
                .sorted(Comparator
                        .comparing(ShowtimeResponse::getShowDate)
                        .thenComparing(ShowtimeResponse::getStartTime))
                .collect(Collectors.toList());
    }

    public ShowtimeResponse getShowtimeById(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));

        return mapToResponse(showtime);
    }

    public ShowtimeResponse updateShowtime(Long showtimeId, ShowtimeUpdateRequest request) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));

        // Kiểm tra và cập nhật movie nếu movieId thay đổi
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new AppException(ErrolCode.MOVIE_NOT_FOUND));
        showtime.setMovie(movie);

        // Tính endTime
        LocalTime endTime = showtime.getStartTime().plusMinutes(movie.getDuration());
        showtime.setEndTime(endTime);

        // Kiểm tra và cập nhật room nếu roomId thay đổi
        if (request.getRoomId() != null && !request.getRoomId().equals(showtime.getRoom() != null ? showtime.getRoom().getRoomId() : null)) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new AppException(ErrolCode.ROOM_NOT_FOUND));

            // Cập nhật logic tương tự cho update nếu cần thiết
            if (!"ACTIVE".equalsIgnoreCase(room.getStatus())) {
                throw new RuntimeException("Không thể chuyển suất chiếu sang phòng đang bảo trì hoặc ngưng hoạt động");
            }

            showtime.setRoom(room);
        }

        // Cập nhật các trường khác
        showtimeMapper.updateShowtime(showtime, request);

        // Lưu thay đổi vào database
        showtimeRepository.save(showtime);

        // Lấy lại showtime từ database để đảm bảo dữ liệu mới (đặc biệt là các quan hệ)
        Showtime updatedShowtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));

        return mapToResponse(updatedShowtime);
    }

    public void deleteShowtime(Long showtimeId) {
        showtimeRepository.deleteById(showtimeId);
    }
}