package com.example.demo.repository;

import com.example.demo.entity.Room;
import com.example.demo.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s JOIN s.room r WHERE r.name = :name")
    List<Showtime> findByRoomName(@Param("name") String name);

    List<Showtime> findByRoomRoomId(Long roomId); // Phương thức mới
    List<Showtime> findByMovieMovieId(Long movieId);

    @Query("SELECT s FROM Showtime s WHERE s.room.roomId = :roomId AND s.showDate = :showDate " +
            "AND ((s.startTime <= :endTime AND s.endTime >= :startTime) " +
            "OR (s.startTime >= :startTime AND s.startTime < :endTime))")
    List<Showtime> findOverlappingShowtime(Long roomId, LocalDate showDate, LocalTime startTime, LocalTime endTime);
}
