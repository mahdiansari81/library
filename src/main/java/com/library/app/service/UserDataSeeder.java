package com.library.app.service;

import com.library.app.entity.User;
import com.library.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class UserDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-user.username}")
    private String defaultUsername;

    @Value("${app.default-user.password}")
    private String defaultPassword;

    @Value("${app.default-user.mobile}")
    private String defaultMobile;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(defaultUsername).isPresent()) {
            log.info("[UserDataSeeder] کاربر پیش‌فرض '{}' از قبل وجود داره. Skip کردن.", defaultUsername);
            return;
        }

        log.info("[UserDataSeeder] ساخت کاربر پیش‌فرض: {}", defaultUsername);

        User user = User.builder()
                .username(defaultUsername)
                .mobile(defaultMobile)
                .password(passwordEncoder.encode(defaultPassword))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("[UserDataSeeder] ✅ کاربر پیش‌فرض '{}' ساخته شد!", defaultUsername);
    }
}