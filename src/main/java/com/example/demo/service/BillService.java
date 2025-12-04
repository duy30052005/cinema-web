package com.example.demo.service;

import com.example.demo.dto.request.BillCreationRequest;
import com.example.demo.dto.request.BillUpdateRequest;
import com.example.demo.dto.response.BillComboResponse;
import com.example.demo.dto.response.BillResponse;
import com.example.demo.dto.response.TicketResponse;
import com.example.demo.entity.*;
import com.example.demo.mapper.BillComboMapper;
import com.example.demo.mapper.BillMapper;
import com.example.demo.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillService {
    BillRepository billRepository;
    UserRepository userRepository;
    BillMapper billMapper;
    private final BillComboMapper billComboMapper;
    private final TicketRepository ticketRepository;
    private final BillComboRepository billComboRepository;
    private final ComboRepository comboRepository;
    JavaMailSender mailSender; // Tiêm JavaMailSender
    private final ShowtimeRepository showtimeRepository;

    @Transactional
    public BillResponse createBill(BillCreationRequest request) {
        log.info("Create bill for userId: {}", request.getUserId());
        User user = userRepository.findById(request.getUserId()).orElse(null);
        // Tạo bill rỗng
        Bill bill = Bill.builder()
                .user(user)
                .paymentStatus("Pending")
                .totalAmount(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();
        bill = billRepository.save(bill);

        BillResponse billResponse = billMapper.toBillResponse(bill);
        if (bill.getUser()!=null){
            billResponse.setUserId(bill.getUser().getUserId());
        }
        billResponse.setTotalAmount(bill.getTotalAmount());

        return billResponse;
    }
    @Transactional
    public BillResponse updateBill(Long billId, BillUpdateRequest request) {
        log.info("Update total amount for billId: {}", billId);
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        User user = userRepository.findById(request.getUserId()).orElse(null);
        List<Ticket> tickets= ticketRepository.findByBillBillId(billId);
        List<BillCombo> billCombos= billComboRepository.findByBillBillId(billId);
        if (request.getUserId()!=null){
            bill.setUser(user);
        }
        BigDecimal totalTickets = BigDecimal.ZERO;
        if (tickets!=null) {
            for (Ticket ticket : tickets) {
                totalTickets = totalTickets.add(ticket.getPrice()); // Gán lại kết quả của add()            }
            }
        }
        BigDecimal totalBillCombos = BigDecimal.ZERO;
        if (billCombos!=null){
            for (BillCombo billCombo : billCombos) {
                totalBillCombos=totalBillCombos.add(billCombo.getTotalPrice());
            }
        }
        bill.setTotalAmount(totalBillCombos.add(totalTickets));
        bill.setPaymentMethod(request.getPaymentMethod());
        if(request.getPaymentStatus()!=null&&request.getPaymentStatus().equals("DONE")){
            bill.setPaymentStatus(request.getPaymentStatus());
            bill.setPaymentAt(LocalDateTime.now());
        }
        billRepository.save(bill);
        BillResponse billResponse = billMapper.toBillResponse(bill);
        if (bill.getUser()!=null){
            billResponse.setUserId(bill.getUser().getUserId());
        }
        billResponse.setTotalAmount(bill.getTotalAmount());
        // Ánh xạ tickets
        if (bill.getTickets() != null) {
            List<TicketResponse> ticketResponses = tickets.stream()
                    .map(ticket -> {
                        TicketResponse response = new TicketResponse();
                        response.setTicketId(ticket.getTicketId());
                        response.setUserId(ticket.getUser() != null ? ticket.getUser().getUserId() : null);
                        response.setSeatId(ticket.getSeat() != null ? ticket.getSeat().getSeatId() : null);
                        response.setShowtimeId(ticket.getShowtime() != null ? ticket.getShowtime().getShowtimeId() : null);
                        response.setBookingDate(ticket.getBookingDate());
                        response.setTicketName(ticket.getTicketName());
                        response.setPrice(ticket.getPrice());
                        response.setBillId(ticket.getBill() != null ? ticket.getBill().getBillId() : null);
                        return response;
                    })
                    .collect(Collectors.toList());
            billResponse.setTicketIds(ticketResponses);
        }

        // Ánh xạ billCombos
        if (bill.getBillCombos() != null) {
            List<BillComboResponse> billComboResponses = billCombos.stream()
                    .map(billCombo -> {
                        BillComboResponse response = new BillComboResponse();
                        response.setBillComboId(billCombo.getBillComboId());
                        response.setBillId(billCombo.getBill() != null ? billCombo.getBill().getBillId() : null);
                        response.setComboId(billCombo.getCombo() != null ? billCombo.getCombo().getComboId() : null);
                        response.setQuantity(billCombo.getQuantity());
                        response.setTotalPrice(billCombo.getTotalPrice());
                        return response;
                    })
                    .collect(Collectors.toList());
            billResponse.setBillComboIds(billComboResponses);
        }
        billResponse.setPaymentAt(bill.getPaymentAt());
        // Kiểm tra điều kiện: tất cả các trường không null
        if (billResponse.getUserId() != null &&
                billResponse.getPaymentMethod() != null &&
                billResponse.getPaymentStatus().equalsIgnoreCase("DONE") &&
                billResponse.getTicketIds() != null &&
                billResponse.getBillComboIds() != null) {
            sendEmailNotification(billId, billResponse);
        }
        return billResponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<BillResponse> getAllBills() {
        log.info("Fetching all bills");
        List<Bill> bills = billRepository.findAll();
        return bills.stream()
                .map(bill -> {
                    BillResponse billResponse = billMapper.toBillResponse(bill);
                    if (bill.getUser() != null) {
                        billResponse.setUserId(bill.getUser().getUserId());
                    }
                    billResponse.setTotalAmount(bill.getTotalAmount());

                    // Lấy danh sách tickets cho mỗi bill
                    List<Ticket> tickets = ticketRepository.findByBillBillId(bill.getBillId());
                    if (tickets != null) {
                        List<TicketResponse> ticketResponses = tickets.stream()
                                .map(ticket -> {
                                    TicketResponse response = new TicketResponse();
                                    response.setTicketId(ticket.getTicketId());
                                    response.setUserId(ticket.getUser() != null ? ticket.getUser().getUserId() : null);
                                    response.setSeatId(ticket.getSeat() != null ? ticket.getSeat().getSeatId() : null);
                                    response.setShowtimeId(ticket.getShowtime() != null ? ticket.getShowtime().getShowtimeId() : null);
                                    response.setBookingDate(ticket.getBookingDate());
                                    response.setTicketName(ticket.getTicketName());
                                    response.setPrice(ticket.getPrice());
                                    response.setBillId(ticket.getBill() != null ? ticket.getBill().getBillId() : null);
                                    return response;
                                })
                                .collect(Collectors.toList());
                        billResponse.setTicketIds(ticketResponses);
                    }

                    // Lấy danh sách billCombos cho mỗi bill
                    List<BillCombo> billCombos = billComboRepository.findByBillBillId(bill.getBillId());
                    if (billCombos != null) {
                        List<BillComboResponse> billComboResponses = billCombos.stream()
                                .map(billCombo -> {
                                    BillComboResponse response = new BillComboResponse();
                                    response.setBillComboId(billCombo.getBillComboId());
                                    response.setBillId(billCombo.getBill() != null ? billCombo.getBill().getBillId() : null);
                                    response.setComboId(billCombo.getCombo() != null ? billCombo.getCombo().getComboId() : null);
                                    response.setQuantity(billCombo.getQuantity());
                                    response.setTotalPrice(billCombo.getTotalPrice());
                                    return response;
                                })
                                .collect(Collectors.toList());
                        billResponse.setBillComboIds(billComboResponses);
                    }

                    return billResponse;
                })
                .collect(Collectors.toList());
    }
    public BillResponse getBillById(Long billId) {
        log.info("Fetching bill by id: " + billId);
        Bill bill = billRepository.findById(billId).orElse(null);
        BillResponse billResponse = billMapper.toBillResponse(bill);
        if (bill.getUser() != null) {
            billResponse.setUserId(bill.getUser().getUserId());
        }
        billResponse.setTotalAmount(bill.getTotalAmount());

        // Lấy danh sách tickets cho mỗi bill
        List<Ticket> tickets = ticketRepository.findByBillBillId(bill.getBillId());
        if (tickets != null) {
            List<TicketResponse> ticketResponses = tickets.stream()
                    .map(ticket -> {
                        TicketResponse response = new TicketResponse();
                        response.setTicketId(ticket.getTicketId());
                        response.setUserId(ticket.getUser() != null ? ticket.getUser().getUserId() : null);
                        response.setSeatId(ticket.getSeat() != null ? ticket.getSeat().getSeatId() : null);
                        response.setShowtimeId(ticket.getShowtime() != null ? ticket.getShowtime().getShowtimeId() : null);
                        response.setBookingDate(ticket.getBookingDate());
                        response.setTicketName(ticket.getTicketName());
                        response.setPrice(ticket.getPrice());
                        response.setBillId(ticket.getBill() != null ? ticket.getBill().getBillId() : null);
                        return response;
                    })
                    .collect(Collectors.toList());
            billResponse.setTicketIds(ticketResponses);
        }

        // Lấy danh sách billCombos cho mỗi bill
        List<BillCombo> billCombos = billComboRepository.findByBillBillId(bill.getBillId());
        if (billCombos != null) {
            List<BillComboResponse> billComboResponses = billCombos.stream()
                    .map(billCombo -> {
                        BillComboResponse response = new BillComboResponse();
                        response.setBillComboId(billCombo.getBillComboId());
                        response.setBillId(billCombo.getBill() != null ? billCombo.getBill().getBillId() : null);
                        response.setComboId(billCombo.getCombo() != null ? billCombo.getCombo().getComboId() : null);
                        response.setQuantity(billCombo.getQuantity());
                        response.setTotalPrice(billCombo.getTotalPrice());
                        return response;
                    })
                    .collect(Collectors.toList());
            billResponse.setBillComboIds(billComboResponses);
        }

        return billResponse;
    }
    // Phương thức gửi email
    private void sendEmailNotification(Long billId, BillResponse billResponse) {
        log.info("Sending email notification for billId: {}", billId);
        User user = userRepository.findById(billResponse.getUserId()).orElse(null);
        String userName = user != null ? user.getUsername() : "Unknown User";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail()); // Thay bằng email người nhận
        message.setSubject("Thông tin hóa đơn");
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Xin chào ").append(userName).append(",\n\nHóa Đơn với ID ").append(billId).append(" đã đặt thành công.\n")
                .append("Details:\n")
                .append("- Tổng số tiền: ").append(billResponse.getTotalAmount()).append("\n")
                .append("- Phương thức thanh toán: ").append(billResponse.getPaymentMethod()).append("\n")
                .append("- Trạng thái thanh toán: ").append(billResponse.getPaymentStatus()).append("\n");

        // Thêm chi tiết tickets
        if (billResponse.getTicketIds() != null && !billResponse.getTicketIds().isEmpty()) {
            emailBody.append("- Vé:\n");
            for (TicketResponse ticket : billResponse.getTicketIds()) {
                emailBody.append("  - Tên vé: ").append(ticket.getTicketName())
                        .append(", Giá: ").append(ticket.getPrice()).append("\n");
            }
        }

        // Thêm chi tiết billCombos
        if (billResponse.getBillComboIds() != null && !billResponse.getBillComboIds().isEmpty()) {
            emailBody.append("- Combo:\n");
            for (BillComboResponse billCombo : billResponse.getBillComboIds()) {
                Combo combo=comboRepository.findById(billCombo.getComboId()).orElse(null);
                emailBody.append("  - Tên combo: ").append(combo.getName())
                        .append(", Số lượng: ").append(billCombo.getQuantity())
                        .append(", Tổng giá: ").append(billCombo.getTotalPrice())
                        .append("\n");
            }
        }

        emailBody.append("\nCảm ơn bạn!");

        message.setText(emailBody.toString());
        mailSender.send(message);
        log.info("Email notification sent for billId: {}", billId);
    }
    // Scheduler để kiểm tra và xóa bill quá hạn
    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi 1 phút (60000 ms)
    @Transactional
    public void checkAndDeleteExpiredBills() {
        log.info("Checking for expired bills at {}", LocalDateTime.now());
        List<Bill> pendingBills = billRepository.findByPaymentStatus("Pending");
        log.info("Found {} pending bills", pendingBills.size());
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        for (Bill bill : pendingBills) {
            if (bill.getCreatedAt().isBefore(fiveMinutesAgo)) {
                log.info("Deleting expired bill with billId: {}", bill.getBillId());
                // Xóa các Ticket liên quan
                ticketRepository.deleteByBillBillId(bill.getBillId());
                // Xóa các BillCombo liên quan
                billComboRepository.deleteByBillBillId(bill.getBillId());
                // Xóa Bill
                billRepository.deleteById(bill.getBillId()); // Thay delete(bill) bằng deleteById
            }
        }
    }

}
