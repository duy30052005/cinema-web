package com.example.demo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieUpdateRequest {
    long movie_id;


    String title;

    String description;

    int duration;

    @NotNull
    LocalDate releaseDate;

    @Min(value = 0)
    @Max(value = 10)
    double rating;

    Set<String> typeNames;

    String image;

    String trailer;

    String director;
}
