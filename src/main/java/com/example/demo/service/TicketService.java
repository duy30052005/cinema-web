package com.example.demo.service;

import com.example.demo.dto.request.TicketCreationRequest;
import com.example.demo.dto.response.TicketResponse;
import com.example.demo.entity.*;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TicketService {
    TicketMapper ticketMapper;
    TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final BillRepository billRepository;
    private final BillService billService;

    public TicketResponse create(TicketCreationRequest request){
        log.info("Create ticket");
        User user=userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrolCode.USER_NOT_FOUND));
        Seat seat=seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new AppException(ErrolCode.SEAT_NOT_FOUND));
        Showtime showtime=showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new AppException(ErrolCode.SHOWTIME_NOT_FOUND));
        // Kiểm tra xem đã có ticket với cùng showtimeId và seatId chưa
        ticketRepository.findByShowtimeShowtimeIdAndSeatSeatId(request.getShowtimeId(), request.getSeatId())
                .ifPresent(ticket -> {
                    throw new AppException(ErrolCode.SEAT_ALREADY_BOOKED);
                });
        if (!showtime.getRoom().getRoomId().equals(seat.getRoom().getRoomId())){
            throw new AppException(ErrolCode.INVALID_ROOM_MATCH);
        }
        // Lấy bill từ billId (nếu có)
        Bill bill = null;
        if (request.getBillId() != null) {
            bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new AppException(ErrolCode.BILL_NOT_FOUND));
        }
        Ticket ticket=ticketMapper.toTicket(request);
        ticket.setUser(user);
        ticket.setSeat(seat);
        ticket.setShowtime(showtime);
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setBill(bill);
        ticketRepository.save(ticket);

//        // Cập nhật total_amount của bill nếu có
//        if (bill != null) {
//            billService.updateBillTotal(bill.getBillId());
//        }
        TicketResponse ticketResponse=ticketMapper.toTicketResponse(ticket);
        if(ticket.getUser()!=null){
            ticketResponse.setUserId(ticket.getUser().getUserId());
        }
        if(ticket.getSeat()!=null){
            ticketResponse.setSeatId(ticket.getSeat().getSeatId());

        }
        if (ticket.getShowtime()!=null){
            ticketResponse.setShowtimeId(ticket.getShowtime().getShowtimeId());
        }
        if (ticket.getBill()!=null){
            ticketResponse.setBillId(ticket.getBill().getBillId());
        }
        return ticketResponse;
    }
    public List<TicketResponse> getTicketByShowtime(Long showtimeId){
        List<Ticket> tickets= ticketRepository.findByShowtimeShowtimeId(showtimeId);

        return tickets.stream()
                .map(ticket -> {
                    TicketResponse response=ticketMapper.toTicketResponse(ticket);
                    if(ticket.getUser()!=null){
                        response.setUserId(ticket.getUser().getUserId());
                    }
                    if(ticket.getSeat()!=null){
                        response.setSeatId(ticket.getSeat().getSeatId());
                    }
                    if (ticket.getShowtime()!=null){
                        response.setShowtimeId(ticket.getShowtime().getShowtimeId());
                    }
                    if (ticket.getBill() != null) {
                        response.setBillId(ticket.getBill().getBillId());
                    }
                    response.setBookingDate(ticket.getBookingDate());
                    return response;
                })
                .collect(Collectors.toList());
    }
    public List<TicketResponse> getTicketByUser(Long userId){
        List<Ticket> tickets= ticketRepository.findByUserUserId(userId);
        return tickets.stream()
                .map(ticket -> {
                    TicketResponse response=ticketMapper.toTicketResponse(ticket);
                    if(ticket.getUser()!=null){
                        response.setUserId(ticket.getUser().getUserId());
                    }
                    if(ticket.getSeat()!=null){
                        response.setSeatId(ticket.getSeat().getSeatId());
                    }
                    if (ticket.getShowtime()!=null){
                        response.setShowtimeId(ticket.getShowtime().getShowtimeId());
                    }
                    if (ticket.getBill() != null) {
                        response.setBillId(ticket.getBill().getBillId());
                    }
                    response.setBookingDate(ticket.getBookingDate());
                    return response;
                })
                .collect(Collectors.toList());
    }
    public List<TicketResponse> getTicketByBill(Long billId){
        List<Ticket> tickets=ticketRepository.findByBillBillId(billId);
        return tickets.stream()
                .map(ticket -> {
                    TicketResponse response=ticketMapper.toTicketResponse(ticket);
                    if(ticket.getUser()!=null){
                        response.setUserId(ticket.getUser().getUserId());
                    }
                    if(ticket.getSeat()!=null){
                        response.setSeatId(ticket.getSeat().getSeatId());
                    }
                    if (ticket.getShowtime()!=null){
                        response.setShowtimeId(ticket.getShowtime().getShowtimeId());
                    }
                    if (ticket.getBill() != null) {
                        response.setBillId(ticket.getBill().getBillId());
                    }
                    response.setBookingDate(ticket.getBookingDate());
                    return response;
                })
                .collect(Collectors.toList());
    }

}
