package com.library.app.service;

import com.library.app.dto.UserRequestDto;
import com.library.app.dto.UserResponseDto;
import com.library.app.entity.User;
import com.library.app.exception.DuplicateResourceException;
import com.library.app.exception.ResourceNotFoundException;
import com.library.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponseDto createUser(UserRequestDto dto) {
        userRepository.findByUsername(dto.getUsername())
                .ifPresent(u -> {
                    throw new DuplicateResourceException("کاربری با این نام کاربری قبلاً ثبت شده");
                });

        User user = User.builder()
                .username(dto.getUsername())
                .mobile(dto.getMobile())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        User saved = userRepository.save(user);
        return mapToDto(saved);
    }


    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("کاربری با این id پیدا نشد"));
        return mapToDto(user);
    }


    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToDto);
    }


    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .build();
    }
}