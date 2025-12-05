package com.example.demo.controller;

import com.example.demo.dto.request.ApiResponse;
import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
//    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.createRequest(request));

        return apiResponse;
    }
    @GetMapping
    List<User> getAllUsers()
             {
        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities()
                .forEach((grantedAuthority) -> log.info(grantedAuthority.getAuthority()));

        return userService.getAllUsers();
    }
    @GetMapping("/myInfo")
    UserResponse getMyInfo() {
        return userService.getMyInfo();
    }
    @GetMapping("/{user_id}")
    UserResponse getUserById(@PathVariable("user_id") Long user_id) {


        return userService.getUserById(user_id);
    }
    @PutMapping("/{user_id}")
    UserResponse updateUser(@PathVariable("user_id") Long user_id, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(request,user_id);
    }
    @DeleteMapping("/{user_id}")
    String deleteUser(@PathVariable("user_id") Long user_id) {
        userService.deleteUser(user_id);
        return "User deleted";
    }

}
