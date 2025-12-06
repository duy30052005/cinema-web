package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeResponse {
    long showtimeId;

    String title;//tên phim

    long roomId;

    String name;//tên phòng

    String cinemaName;

    LocalDate showDate;

    String status;

    @JsonFormat(pattern = "HH:mm") // Định dạng thời gian thành HH:mm
    LocalTime startTime;

    @JsonFormat(pattern = "HH:mm") // Định dạng thời gian thành HH:mm
    LocalTime endTime;
}
