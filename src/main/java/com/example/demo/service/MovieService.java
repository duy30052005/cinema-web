package com.example.demo.service;

import com.example.demo.dto.request.MovieCreationRequest;
import com.example.demo.dto.request.MovieUpdateRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.MovieMapper;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.TypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MovieService {
    MovieRepository movieRepository;
    TypeRepository typeRepository;
    MovieMapper movieMapper;

    public MovieResponse create(MovieCreationRequest request) {
        Movie movie = movieMapper.toMovie(request);

        // Xử lý việc gắn các loại phim (types)
        if (request.getTypeNames() != null && !request.getTypeNames().isEmpty()) {
            Set<Type> types = new HashSet<>();
            for (String typeName : request.getTypeNames()) {
                Type type = typeRepository.findByTypename(typeName)
                        .orElseGet(() -> {
                            Type newType = Type.builder().typename(typeName).build();
                            return typeRepository.save(newType);
                        });
                types.add(type);
            }
            movie.setTypes(types);
        }

        movie = movieRepository.save(movie); // Lưu movie với các types đã gắn

        return movieMapper.toMovieResponse(movie);
    }
    public List<MovieResponse> getAllMovie() {
        log.info("Get all movie");
        List<Movie> movies = movieRepository.findAll(); // Lấy danh sách entity

        return movies.stream()
                .map(movieMapper::toMovieResponse) // Map từng cái sang response
                .collect(Collectors.toList());

    }

    public MovieResponse getMovieById(Long movie_id) {
        log.info("Get movie by title: {}", movie_id);
        Movie movie = movieRepository.findById(movie_id)
                .orElseThrow(() -> new AppException(ErrolCode.MOVIE_NOT_FOUND)
        );
        Set<Type> types = movie.getTypes(); // Lấy danh sách Type
        return movieMapper.toMovieResponse(movie);
    }

    public MovieResponse updateRequest(MovieUpdateRequest request, long movieId){
        Movie movie=movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movieMapper.updateMovie(movie,request);

        // Xử lý việc gắn các loại phim (types) tương tự như phương thức create
        if (request.getTypeNames() != null && !request.getTypeNames().isEmpty()) {
            Set<Type> types = new HashSet<>();
            for (String typeName : request.getTypeNames()) {
                Type type = typeRepository.findByTypename(typeName)
                        .orElseGet(() -> {
                            Type newType = Type.builder().typename(typeName).build();
                            return typeRepository.save(newType);
                        });
                types.add(type);
            }
            movie.setTypes(types);
        }

        log.info("Update movie {} successful", movie.getTitle());
        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }
    public void deleteMovie(Long movie_id) {
        movieRepository.deleteById(movie_id);
    }


}
