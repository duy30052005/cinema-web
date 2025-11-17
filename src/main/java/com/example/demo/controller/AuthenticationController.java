package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.athenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> checkAuthentication(@RequestBody IntrospectRequest request)
            throws ParseException , JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/forgotpassword")
    ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String result = authenticationService.requestPasswordReset(request.getGmail());
            return ApiResponse.<String>builder()
                    .result(result)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.<String>builder()
                    .code(400)
                    .message("Lỗi khi gửi mã xác minh: " + e.getMessage())
                    .build();

        }
    }

    @PostMapping("/updatepassword")
    ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String result = authenticationService.resetPassword(request.getCode(), request.getNewPassword());
        if (result.contains("thành công")) {
            return ApiResponse.<String>builder()
                    .result(result)
                    .build();
        }
        return ApiResponse.<String>builder()
                .code(400)
                .message(result)
                .build();
    }

    @Data
    static class ForgotPasswordRequest {
        private String gmail;
    }

    @Data
    static class ResetPasswordRequest {
        private String code;
        private String newPassword;
    }
}
