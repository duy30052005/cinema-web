package com.example.demo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
    int movieId;

    String title;

    String description;

    int duration;

    LocalDate releaseDate;

    double rating;

    Set<String> types;

    String image;

    String trailer;

    String director;

}
