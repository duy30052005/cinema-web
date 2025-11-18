package com.example.demo.exception;

import com.example.demo.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
        ApiResponse apiResponse=new ApiResponse();

        apiResponse.setCode(ErrolCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrolCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrolCode errolCode= exception.getErrolCode();
        ApiResponse apiResponse=new ApiResponse();

        apiResponse.setCode(errolCode.getCode());
        apiResponse.setMessage(errolCode.getMessage());

        return ResponseEntity
                .status(errolCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrolCode errolCode= ErrolCode.FORBIDDEN;

        return ResponseEntity.status(errolCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errolCode.getCode())
                        .message(errolCode.getMessage())
                        .build()
        );

    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        String enumKey=exception.getFieldError().getDefaultMessage();
        ErrolCode errolCode= ErrolCode.KEY_INVALID;
        try {
            errolCode=ErrolCode.valueOf(enumKey);
        }catch (IllegalArgumentException e){

        }


        ApiResponse apiResponse=new ApiResponse();

        apiResponse.setCode(errolCode.getCode());
        apiResponse.setMessage(errolCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


}
