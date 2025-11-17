package com.example.demo.dto.request;

import com.example.demo.entity.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieCreationRequest {
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
