package com.example.demo.mapper;

import com.example.demo.dto.request.MovieCreationRequest;
import com.example.demo.dto.request.MovieUpdateRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.MovieResponse;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toMovie(MovieCreationRequest request);
    void updateMovie(@MappingTarget Movie movie, MovieUpdateRequest request);
    MovieResponse toMovieResponse(Movie movie);

    //hướng dẫn MapStruct cách chuyển Type thành String
    default Set<String> map(Set<Type> types) {
        if (types == null) return null;
        return types.stream()
                .map(Type::getTypename) // map từ entity Type → tên
                .collect(Collectors.toSet());
    }

}
