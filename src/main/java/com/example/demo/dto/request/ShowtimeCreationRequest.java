package com.example.demo.dto.request;

import com.example.demo.entity.Movie;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeCreationRequest {

    long movieId;

    long roomId;

    LocalDate showDate;

    LocalTime startTime;
}
