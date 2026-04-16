package com.smartbiz.service.impl;

import com.smartbiz.dto.AuthResponseDto;
import com.smartbiz.dto.LoginRequestDto;
import com.smartbiz.dto.RegisterRequestDto;
import com.smartbiz.entity.Business;
import com.smartbiz.entity.User;
import com.smartbiz.enums.SubscriptionPlan;
import com.smartbiz.enums.UserRole;
import com.smartbiz.repository.BusinessRepository;
import com.smartbiz.repository.UserRepository;
import com.smartbiz.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           BusinessRepository businessRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponseDto(null, "Email already exists");
        }

        Business business = Business.builder()
                .businessName(request.getBusinessName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .subscriptionPlan(SubscriptionPlan.FREE)
                .build();

        businessRepository.save(business);

        User owner = User.builder()
                .fullName(request.getOwnerName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(UserRole.OWNER)
                .business(business)
                .build();

        userRepository.save(owner);

        return new AuthResponseDto("dummy-token", "Registration successful");
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            return new AuthResponseDto(null, "User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponseDto(null, "Invalid password");
        }

        return new AuthResponseDto("dummy-token", "Login successful");
    }
}