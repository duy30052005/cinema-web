package com.example.demo.service;

import com.example.demo.dto.request.BillComboCreationRequest;
import com.example.demo.dto.response.BillComboResponse;
import com.example.demo.entity.Bill;
import com.example.demo.entity.BillCombo;
import com.example.demo.entity.Combo;
import com.example.demo.mapper.BillComboMapper;
import com.example.demo.repository.BillComboRepository;
import com.example.demo.repository.BillRepository;
import com.example.demo.repository.ComboRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillComboService {
    BillComboRepository billComboRepository;
    BillRepository billRepository;
    ComboRepository comboRepository;
    BillComboMapper billComboMapper;
    BillService billService;

    @Transactional
    public BillComboResponse createBillCombo(BillComboCreationRequest request) {
        log.info("Create bill combo for billId: {} and comboId: {}", request.getBillId(), request.getComboId());

        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        Combo combo = comboRepository.findById(request.getComboId())
                .orElseThrow(() -> new RuntimeException("Combo not found"));

        BillCombo billCombo = billComboMapper.toBillCombo(request);
        billCombo.setBill(bill);
        billCombo.setCombo(combo);
        billCombo.setTotalPrice(combo.getPrice().multiply(new BigDecimal(request.getQuantity()))); // Tính total_price
        billCombo = billComboRepository.save(billCombo);

        BillComboResponse billComboResponse = billComboMapper.toBillComboResponse(billCombo);
        if (billCombo.getBill()!=null){
            billComboResponse.setBillId(billCombo.getBill().getBillId());
        }
        if (billCombo.getCombo()!=null){
            billComboResponse.setComboId(billCombo.getCombo().getComboId());
        }
        return billComboResponse;
    }
    public List<BillComboResponse> getBillCombosByBillId(Long billId) {
        List<BillCombo> billCombos= billComboRepository.findByBillBillId(billId);
        return billCombos.stream()
                .map(billCombo -> {
                    BillComboResponse response = billComboMapper.toBillComboResponse(billCombo);
                    if(billCombo.getCombo()!=null){
                        response.setComboId(billCombo.getCombo().getComboId());
                    }
                    if(billCombo.getBill()!=null){
                        response.setBillId(billCombo.getBill().getBillId());
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }
}
