package com.example.demo.service;

import com.example.demo.dto.request.ComboCreationRequest;
import com.example.demo.dto.request.ComboUpdateRequest;
import com.example.demo.dto.response.ComboResponse;
import com.example.demo.entity.Combo;
import com.example.demo.mapper.ComboMapper;
import com.example.demo.repository.ComboRepository;
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
public class ComboService {
    private final ComboMapper comboMapper;
    private final ComboRepository comboRepository;

    public ComboResponse createCombo(ComboCreationRequest request){
        log.info("Create combo");
        Combo combo= comboMapper.toCombo(request);
        comboRepository.save(combo);
        return comboMapper.toComboResponse(combo);
    }
    public ComboResponse updateCombo(ComboUpdateRequest request, Long comboId){
        log.info("Update combo");
        Combo combo = comboRepository.findById(comboId).orElse(null);
        comboMapper.updateCombo(combo, request);
        comboRepository.save(combo);
        return comboMapper.toComboResponse(combo);
    }
    public List<ComboResponse> getAllCombos(){
        log.info("Get all combos");
        List<Combo> combos = comboRepository.findAll();
        return combos.stream()
                .map(comboMapper::toComboResponse)
                .collect(Collectors.toList());
    }
    public ComboResponse getComboById(Long comboId){
        log.info("Get combo by id");
        Combo combo = comboRepository.findById(comboId).orElse(null);
        return comboMapper.toComboResponse(combo);
    }
    public void deleteComboById(Long comboId){
        log.info("Delete combo by id");
        comboRepository.deleteById(comboId);

    }
}
