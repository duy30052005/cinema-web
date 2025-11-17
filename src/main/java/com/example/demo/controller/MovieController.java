package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.MovieCreationRequest;
import com.example.demo.dto.request.MovieUpdateRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.entity.Movie;
import com.example.demo.service.MovieService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MovieController {
    MovieService movieService;


    @PostMapping
    ApiResponse<MovieResponse> createMovie(@RequestBody @Valid MovieCreationRequest request){
        return ApiResponse.<MovieResponse>builder()
                .result(movieService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<MovieResponse>> getAllMovies(){
        return ApiResponse.<List<MovieResponse>>builder()
                .result(movieService.getAllMovie())
                .build();
    }
    @GetMapping("/{movie_id}")
    MovieResponse getMovieByTitle(@PathVariable("movie_id") Long movie_id){
        return movieService.getMovieById(movie_id);
    }
    @PutMapping("{movie_id}")
    MovieResponse updateMovie(@PathVariable("movie_id") Long movie_id,@RequestBody @Valid MovieUpdateRequest request){
        return movieService.updateRequest(request,movie_id);
    }
    @DeleteMapping("/{movie_id}")
    ApiResponse<Void> deleteMovie(@PathVariable("movie_id") Long movie_id) {
        movieService.deleteMovie(movie_id);
        return ApiResponse.<Void>builder().build();
    }
}
