package com.example.demo.service;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrolCode;
import com.example.demo.repository.UserRepository;
import com.example.demo.storage.ResetCodeStorage;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    JavaMailSender mailSender;
    ResetCodeStorage codeStorage;
    PasswordEncoder passwordEncoder;


    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException , ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified= signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expirationDate.after(new Date()))
                .build();
    }

    public AuthenticationResponse athenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrolCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches((request.getPassword()),
                user.getPassword());

        if(!authenticated){
            throw new AppException(ErrolCode.AUTHENTICATION_FAILED);
        }
        var token =generateToken(user);

        return  AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();


    }
    public  String generateToken(User user){
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet= new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("desteria.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(5, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("password", user.getPassword())
                .claim("id",user.getUserId())
                .claim("email",user.getEmail())
                .claim("scope",user.getRole())
                .build();

        Payload payload=new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public void sendResetCodeEmail(String to, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Mã đặt lại mật khẩu");
        helper.setText("Mã đặt lại mật khẩu của bạn là: <b>" + code + "</b>. Mã này có hiệu lực trong 5 phút.", true);
        mailSender.send(message);
    }

    public String requestPasswordReset(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return "Tài khoản không tồn tại.";
        }

        String code = generateRandomCode();
        codeStorage.storeCode(email, code, LocalDateTime.now().plusMinutes(5));
        sendResetCodeEmail(email, code);
        return "Mã đặt lại mật khẩu đã được gửi đến email của bạn.";
    }


    public String resetPassword(String code, String newPassword) {
        log.info("Starting password reset process with code: {}", code);
        String email = codeStorage.validateCode(code);
        if (email == null) {
            log.warn("Code validation failed. Code: {} is invalid or expired", code);
            return "Mã không hợp lệ hoặc đã hết hạn.";
        }
        log.info("Code validated successfully. Email associated: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            log.error("User not found for email: {}", email);
            return "Tài khoản không tồn tại.";
        }
        log.info("User found for email: {}", email);

        User user = userOptional.get();
        if(code=="12345"){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }else{
            log.info("Encoding new password for user: {}", user.getUsername());
            user.setPassword(passwordEncoder.encode(newPassword));
            log.info("Saving updated user to repository for email: {}", email);
            userRepository.save(user);
        }


        log.info("Password reset successful for email: {}", email);
        return "Mật khẩu đã được cập nhật thành công.";
    }

    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

}
