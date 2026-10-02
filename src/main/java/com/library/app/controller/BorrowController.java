package com.library.app.controller;

import com.library.app.dto.BorrowRequestDto;
import com.library.app.dto.BorrowResponseDto;
import com.library.app.service.BorrowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrows")
@Slf4j
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping
    public ResponseEntity<BorrowResponseDto> borrowBook(@Valid @RequestBody BorrowRequestDto dto) {
        log.info("درخواست امانت کتاب {} توسط کاربر {}", dto.getBookId(), dto.getUserId());
        BorrowResponseDto result = borrowService.borrowBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BorrowResponseDto> returnBook(@PathVariable Long id) {
        log.info("درخواست پس دادن امانت با id: {}", id);
        return ResponseEntity.ok(borrowService.returnBook(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowResponseDto> getBorrowById(@PathVariable Long id) {
        log.info("درخواست دریافت امانت با id: {}", id);
        return ResponseEntity.ok(borrowService.getBorrowById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BorrowResponseDto>> getBorrowsByUser(@PathVariable Long userId, Pageable pageable) {
        log.info("درخواست دریافت امانت‌های کاربر با id: {}", userId);
        return ResponseEntity.ok(borrowService.getBorrowsByUser(userId, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<BorrowResponseDto>> getBorrows(Pageable pageable) {
        log.info("درخواست دریافت لیست تمام امانت‌ها");
        return ResponseEntity.ok(borrowService.getBorrows(pageable));
    }

}