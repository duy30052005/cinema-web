package com.example.demo.repository;

import com.example.demo.entity.Room;
import com.example.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByShowtimeShowtimeIdAndSeatSeatId(Long showtimeId, Long seatId);
    List<Ticket> findByShowtimeShowtimeId(Long showtimeId);
    List<Ticket> findByUserUserId(Long userId);

    List<Ticket> findByBillBillId(Long billId);
    @Modifying
    @Query("DELETE FROM Ticket t WHERE t.bill.billId = :billId")
    void deleteByBillBillId(@Param("billId") Long billId);}
