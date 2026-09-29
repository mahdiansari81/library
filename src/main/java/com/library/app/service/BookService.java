package com.library.app.service;

import com.library.app.dto.BookRequestDto;
import com.library.app.dto.BookResponseDto;
import com.library.app.entity.Book;
import com.library.app.repository.BookRepository;
import com.library.app.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;

    // ۱. ساخت کتاب جدید
    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        // چک کن شابک تکراری نباشه
        bookRepository.findByBookCode(dto.getBookCode())
                .ifPresent(b -> {
                    throw new RuntimeException("کتابی با این شابک قبلاً ثبت شده");
                });

        // ساخت Entity از DTO
        Book book = Book.builder()
                .bookName(dto.getBookName())
                .author(dto.getAuthor())
                .bookCode(dto.getBookCode())
                .publishYear(dto.getPublishYear())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getTotalCopies())  // همه موجود
                .build();

        // ذخیره تو دیتابیس
        Book saved = bookRepository.save(book);

        // تبدیل به DTO و برگردوندن
        return mapToDto(saved);
    }

    // ۲. گرفتن کتاب با id
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("کتابی با این id پیدا نشد"));
        return mapToDto(book);
    }

    // ۳. لیست کتاب‌ها
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ۴. ویرایش کتاب
    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("کتابی با این id پیدا نشد"));

        book.setBookName(dto.getBookName());
        book.setAuthor(dto.getAuthor());
        book.setBookCode(dto.getBookCode());
        book.setPublishYear(dto.getPublishYear());

        // اگه تعداد کل عوض شده، اختلاف رو به available اضافه/کم کن
        int diff = dto.getTotalCopies() - book.getTotalCopies();
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(book.getAvailableCopies() + diff);

        Book saved = bookRepository.save(book);
        return mapToDto(saved);
    }

    // ۵. حذف کتاب
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("کتابی با این id پیدا نشد"));

        // چک کن کتاب در حال امانت نباشه
        boolean isBorrowed = !borrowRepository.findByBookId(id).isEmpty();
        if (isBorrowed) {
            throw new RuntimeException("این کتاب در حال امانت است و نمی‌توان حذف کرد");
        }

        bookRepository.delete(book);
    }

    // متد کمکی: تبدیل Entity به DTO
    private BookResponseDto mapToDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .bookName(book.getBookName())
                .author(book.getAuthor())
                .bookCode(book.getBookCode())
                .publishYear(book.getPublishYear())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}