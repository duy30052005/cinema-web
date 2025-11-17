package com.example.demo.service;

import com.example.demo.dto.request.MessageCreationRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class MessageService {
//    MessageRepository messageRepository;
//    SimpMessagingTemplate messagingTemplate;
//    UserRepository userRepository;

//    public MessageResponse sendMessage(MessageCreationRequest request) {
//        User sender= userRepository.findById(request.getSender()).orElseThrow();
//        User recipient= userRepository.findById(request.getRecipient()).orElseThrow();
//        Message message = new Message();
//        message.setMessageText(request.getMessageText());
//        message.setTimestamp(LocalDateTime.now());
//
//        message.setSender(sender);
//        message.setRecipient(recipient);
//
//        messageRepository.save(message);
//
//        MessageResponse response= new MessageResponse();
//        response.setSender(sender.getUserId());
//        response.setRecipient(recipient.getUserId());
//        response.setSenderName(sender.getUsername());
//        response.setRecipientName(recipient.getUsername());
//        response.setMessageText(request.getMessageText());
//        response.setTimestamp(message.getTimestamp());
//
//        // Gửi tin nhắn realtime đến recipient
//        messagingTemplate.convertAndSendToUser(
//                recipient.getUsername(), // Username của người nhận
//                "/queue/messages",      // Điểm đích queue
//                response                 // Dữ liệu gửi
//        );
//        log.info("Sent message from {} to {} at {}", sender.getUsername(), recipient.getUsername(), LocalDateTime.now());
//        return response;
//    }
}
