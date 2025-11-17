package com.example.demo.repository;

import com.example.demo.entity.Bill;
import com.example.demo.entity.BillCombo;
import com.example.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillComboRepository extends JpaRepository<BillCombo, Long> {
    List<BillCombo> findByBillBillId(Long billId);
    @Modifying
    @Query("DELETE FROM BillCombo bc WHERE bc.bill.billId = :billId")
    void deleteByBillBillId(@Param("billId") Long billId);
}
