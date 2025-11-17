package com.example.demo.repository;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPaymentStatus(String paymentStatus);
}
