package com.microservice.authservice.service;

import com.microservice.authservice.dto.PaginatedUserResponse;
import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.model.User;
import com.microservice.authservice.payload.request.LoginRequest;
import com.microservice.authservice.payload.request.SignUpRequest;
import com.microservice.authservice.payload.request.TokenRefreshRequest;
import com.microservice.authservice.payload.response.MessageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    public ResponseEntity<MessageResponse> saveUser(SignUpRequest user);
    public Optional<User> findByUsername(String username);

    /*public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);*/

    ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> refreshtoken(TokenRefreshRequest request);

    PaginatedUserResponse getAllUsers(Pageable pageable);

    UserDto getUserById(Integer userId);

    String deleteById(Integer userId);

    UserDto updateUser(UserDto userDto);

    User findById(int userId);
}
