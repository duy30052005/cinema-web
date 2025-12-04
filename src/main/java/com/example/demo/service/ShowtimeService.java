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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ShowtimeService {
    ShowtimeMapper showtimeMapper;
    ShowtimeRepository showtimeRepository;
    MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ShowtimeResponse create(ShowtimeCreationRequest request){
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new AppException(ErrolCode.MOVIE_NOT_FOUND));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrolCode.ROOM_NOT_FOUND));

        // Tạo Showtime
        Showtime showtime = showtimeMapper.toShowtime(request);
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStatus("ACTIVE");

        // Tính endTime (ví dụ: cộng thêm duration của phim)
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

        // Tạo response từ mapper
        ShowtimeResponse response = showtimeMapper.toShowtimeResponse(showtime);

        // Xử lý thủ công title và name
        if (showtime.getMovie() != null) {
            response.setTitle(showtime.getMovie().getTitle());
        }
        if (showtime.getRoom() != null) {
            response.setName(showtime.getRoom().getName());
        }
        response.setRoomId(showtime.getRoom().getRoomId());
        return response;
    }
    @Transactional(readOnly = true) // Chỉ đọc, không cần thay đổi dữ liệu
    public List<ShowtimeResponse> getAllShowtime(){
        List<Showtime> showtimes= showtimeRepository.findAll();
        return showtimes.stream()
                .map(showtime -> {
                    ShowtimeResponse response = showtimeMapper.toShowtimeResponse(showtime);
                    // Xử lý thủ công title và name
                    if (showtime.getMovie() != null) {
                        response.setTitle(showtime.getMovie().getTitle());
                    }
                    if (showtime.getRoom() != null) {
                        response.setName(showtime.getRoom().getName());
                    }
                    response.setRoomId(showtime.getRoom().getRoomId());
                    return response;
                })
                .sorted(Comparator
                        .comparing(ShowtimeResponse::getShowDate)
                        .thenComparing(ShowtimeResponse::getStartTime))
                .collect(Collectors.toList());
    }
    public List<ShowtimeResponse> getShowtimeByRoom(Long roomId){
        List<Showtime> showtimes= showtimeRepository.findByRoomRoomId(roomId);
        return showtimes.stream()
                .map(showtime -> {
                    ShowtimeResponse response = showtimeMapper.toShowtimeResponse(showtime);
                    // Xử lý thủ công title và name
                    if (showtime.getMovie() != null) {
                        response.setTitle(showtime.getMovie().getTitle());
                    }
                    if (showtime.getRoom() != null) {
                        response.setName(showtime.getRoom().getName());
                    }
                    response.setRoomId(showtime.getRoom().getRoomId());
                    return response;
                })
                .sorted(Comparator
                        .comparing(ShowtimeResponse::getShowDate)
                        .thenComparing(ShowtimeResponse::getStartTime))
                .collect(Collectors.toList());
    }
    public List<ShowtimeResponse> getShowtimebyMovie(Long movieId){
        List<Showtime> showtimes= showtimeRepository.findByMovieMovieId(movieId);
        return showtimes.stream()
                .map(showtime -> {
                    ShowtimeResponse response = showtimeMapper.toShowtimeResponse(showtime);
                    // Xử lý thủ công title và name
                    if (showtime.getMovie() != null) {
                        response.setTitle(showtime.getMovie().getTitle());
                    }
                    if (showtime.getRoom() != null) {
                        response.setName(showtime.getRoom().getName());
                    }
                    response.setRoomId(showtime.getRoom().getRoomId());
                    return response;
                })
                .sorted(Comparator
                        .comparing(ShowtimeResponse::getShowDate)
                        .thenComparing(ShowtimeResponse::getStartTime))
                .collect(Collectors.toList());
    }
    public ShowtimeResponse getShowtimeById(Long showtimeId){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));

        ShowtimeResponse response=showtimeMapper.toShowtimeResponse(showtime);
        response.setName(showtime.getRoom().getName());
        response.setTitle(showtime.getMovie().getTitle());
        response.setRoomId(showtime.getRoom().getRoomId());
        return response;
    }
    public ShowtimeResponse updateShowtime(Long showtimeId, ShowtimeUpdateRequest request){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));

        // Kiểm tra và cập nhật movie nếu movieId thay đổi
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new AppException(ErrolCode.MOVIE_NOT_FOUND));
        showtime.setMovie(movie);
        // Tính endTime (ví dụ: cộng thêm duration của phim)
        LocalTime endTime = showtime.getStartTime().plusMinutes(movie.getDuration());
        showtime.setEndTime(endTime);


        // Kiểm tra và cập nhật room nếu roomId thay đổi
        if (request.getRoomId() != null && !request.getRoomId().equals(showtime.getRoom() != null ? showtime.getRoom().getRoomId() : null)) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new AppException(ErrolCode.ROOM_NOT_FOUND));
            showtime.setRoom(room);
        }

        // Cập nhật các trường khác
        showtimeMapper.updateShowtime(showtime, request);

        // Lưu thay đổi vào database
        showtimeRepository.save(showtime);

        // Lấy lại showtime từ database để đảm bảo dữ liệu mới
        Showtime updatedShowtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));

        // Tạo response từ showtime đã được làm mới
        ShowtimeResponse response = showtimeMapper.toShowtimeResponse(updatedShowtime);

        // Gán title và name từ showtime đã làm mới
        if (updatedShowtime.getMovie() != null) {
            response.setTitle(updatedShowtime.getMovie().getTitle());
        }
        if (updatedShowtime.getRoom() != null) {
            response.setName(updatedShowtime.getRoom().getName());
        }
        response.setRoomId(updatedShowtime.getRoom().getRoomId());
        return response;
    }
    public void deleteShowtime(Long showtimeId){
        showtimeRepository.deleteById(showtimeId);
    }
}
