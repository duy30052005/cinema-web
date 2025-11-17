package com.example.demo.service;

import com.example.demo.dto.request.TypeCreationRequest;
import com.example.demo.dto.response.TypeResponse;
import com.example.demo.entity.Type;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.repository.TypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TypeService {
    TypeRepository typeRepository;
    TypeMapper typeMapper;
    public TypeResponse createType(TypeCreationRequest request){
        Type type=typeMapper.toType(request);
        type=typeRepository.save(type);
        return typeMapper.toTypeResponse(type);
    }
    public List<TypeResponse> getTypeAll(){
        List<Type> typeList=typeRepository.findAll();

        return typeList.stream()
                .map(typeMapper::toTypeResponse)
                .collect(Collectors.toList());
    }
    public void delete(Long id){
        typeRepository.deleteById(id);
    }

}
