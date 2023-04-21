package com.microservice.authservice.service;

import com.microservice.authservice.dto.PaginatedUserResponse;
import com.microservice.authservice.dto.UserDto;
import com.microservice.authservice.exception.RefreshTokenException;
import com.microservice.authservice.exception.RoleException;
import com.microservice.authservice.jwt.JwtUtils;
import com.microservice.authservice.mapper.UserMapper;
import com.microservice.authservice.model.ERole;
import com.microservice.authservice.model.RefreshToken;
import com.microservice.authservice.model.Role;
import com.microservice.authservice.model.User;
import com.microservice.authservice.payload.request.LoginRequest;
import com.microservice.authservice.payload.request.SignUpRequest;
import com.microservice.authservice.payload.request.TokenRefreshRequest;
import com.microservice.authservice.payload.response.JWTResponse;
import com.microservice.authservice.payload.response.MessageResponse;
import com.microservice.authservice.payload.response.TokenRefreshResponse;
import com.microservice.authservice.repository.UserRepository;
import com.microservice.authservice.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

    public ResponseEntity<MessageResponse> saveUser(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(existsByUsername(username)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if(existsByEmail(email)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));

        if (strRoles != null) {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = null;

                        if(roleService.findByName(ERole.ROLE_ADMIN).isEmpty()){
                            adminRole = new Role(ERole.ROLE_ADMIN);
                        }else{
                            adminRole = roleService.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RoleException("Error: Admin Role is not found."));
                        }

                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = null;

                        if(roleService.findByName(ERole.ROLE_USER).isEmpty()){
                            userRole = new Role(ERole.ROLE_USER);
                        }else{
                            userRole = roleService.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RoleException("Error: User Role is not found."));
                        }

                        roles.add(userRole);
                }
            });
        }else{
            roleService.findByName(ERole.ROLE_USER).ifPresentOrElse(roles::add, () -> roles.add(new Role(ERole.ROLE_USER)));
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        JWTResponse jwtResponse = new JWTResponse();
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setToken(jwt);
        jwtResponse.setRefreshToken(refreshToken.getToken());
        jwtResponse.setRoles(roles);

        return ResponseEntity.ok(jwtResponse);
    }

    @Override
    public ResponseEntity<?> refreshtoken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken token = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken + "Refresh token is not in database!"));

        RefreshToken deletedToken = refreshTokenService.verifyExpiration(token);

        User userRefreshToken = deletedToken.getUser();

        String newToken = jwtUtils.generateTokenFromUsername(userRefreshToken.getUsername());

        return ResponseEntity.ok(new TokenRefreshResponse(newToken, requestRefreshToken));
    }

    @Override
    public PaginatedUserResponse getAllUsers(Pageable pageable) {
        Page<User> userEntities = userRepository.findAll(pageable);
        return PaginatedUserResponse.builder().numberOfItems(userEntities.getTotalElements())
                .numberOfPages(userEntities.getTotalPages())
                .userDtoList(userMapper.toUserDtoList(userEntities.getContent()))
                .build();
    }

    @Override
    public UserDto getUserById(Integer userId) {
        Optional<User> userEntity = userRepository.findById(userId);
        if (Objects.nonNull(userEntity)) {
            return userMapper.toUserDto(userEntity.get());
        } else {
            return null;
        }
    }

    @Override
    public String deleteById(Integer userId) {
        try {
            Optional<User> optionalEntity = userRepository.findById(userId);
            if (!optionalEntity.isEmpty() && optionalEntity.isPresent()) {
                userRepository.deleteById(userId);
            } else {
                //logger.error("Record with User Id " + userId + " is not found for Deletion");
                return "Record with User Id " + userId + " is not found for Deletion";
            }
        } catch (Exception exception) {
            //logger.error("Error in Deletion of the record with User Id " + userId + exception);
            return "Record with User Id  " + userId + "is not Deleted";
        }

        return "Record with User Id " + userId + " Deleted Successfully!!!";
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        try {
            Optional<User> existingUserEntity = userRepository.findById(userDto.getId());
            if (Objects.nonNull(existingUserEntity)) {
                User userEntity = userMapper.toUserEntity(userDto);
                userEntity.setId(existingUserEntity.get().getId());
                userRepository.save(userEntity);
                return userMapper.toUserDto(userEntity);
            } else {
                return null;
            }
        } catch (Exception exception) {
            return null;
        }
    }

    public User findById(int userId) {
        return userRepository.findById(userId);
    }
}
