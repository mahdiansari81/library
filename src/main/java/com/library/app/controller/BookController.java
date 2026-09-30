package com.library.app.controller;

import com.library.app.dto.BookRequestDto;
import com.library.app.dto.BookResponseDto;
import com.library.app.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    // ۱. ثبت کتاب جدید
    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@Valid @RequestBody BookRequestDto dto) {
        log.info("درخواست ثبت کتاب جدید: {}", dto.getBookName());
        BookResponseDto created = bookService.createBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ۲. گرفتن کتاب با id
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // ۳. لیست همه‌ی کتاب‌ها
    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(required = false) Boolean available,
            @PageableDefault(size = 20) Pageable pageable) {

        return ResponseEntity.ok(
                bookService.searchBooks(bookName, author, yearFrom, yearTo, available, pageable)
        );
    }

    // ۴. ویرایش کتاب
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }

    // ۵. حذف کتاب
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}