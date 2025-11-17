package com.example.demo.mapper;

import com.example.demo.dto.request.TypeCreationRequest;
import com.example.demo.dto.response.TypeResponse;
import com.example.demo.entity.Type;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    Type toType(TypeCreationRequest request);
    TypeResponse toTypeResponse(Type type);

}
