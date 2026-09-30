package com.library.app.service;

import com.library.app.dto.BorrowRequestDto;
import com.library.app.dto.BorrowResponseDto;
import com.library.app.entity.Book;
import com.library.app.entity.Borrow;
import com.library.app.entity.User;
import com.library.app.enums.Status;
import com.library.app.exception.ConcurrencyException;
import com.library.app.exception.DuplicateResourceException;
import com.library.app.exception.ResourceNotFoundException;
import com.library.app.repository.BookRepository;
import com.library.app.repository.BorrowRepository;
import com.library.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // ۱. امانت دادن کتاب (چالش اصلی!)
    @Transactional
    public BorrowResponseDto borrowBook(BorrowRequestDto dto) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("کتابی با این id پیدا نشد"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("کاربری با این id پیدا نشد"));

        if (book.getAvailableCopies() <= 0) {
            throw new DuplicateResourceException("متأسفانه این کتاب موجود نیست");
        }

        log.info("کاربر {} داره کتاب {} رو امانت می‌گیره", user.getUsername(), book.getBookName());

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        try {
            bookRepository.save(book);
            log.info("کتاب با موفقیت ذخیره شد");
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("تداخل همزمانی! کاربر دیگه‌ای زودتر امانت گرفت");
            throw new ConcurrencyException("کتاب همین الان توسط فرد دیگری امانت رفت. لطفاً دوباره تلاش کن.");
        }

        int borrowDays = dto.getBorrowDays() != null ? dto.getBorrowDays() : 7;

        Borrow borrow = Borrow.builder()
                .traceCode(UUID.randomUUID().toString())
                .user(user)
                .book(book)
                .deliverTime(LocalDateTime.now())
                .returnTime(LocalDateTime.now().plusDays(borrowDays))
                .status(Status.BORROWED)
                .build();

        Borrow saved = borrowRepository.save(borrow);
        log.info("امانت با کد {} ساخته شد", saved.getTraceCode());
        return mapToDto(saved);
    }

    // ۲. پس گرفتن کتاب
    @Transactional
    public BorrowResponseDto returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("امانتی با این id پیدا نشد"));

        if (borrow.getStatus() == Status.RETURNED) {
            throw new DuplicateResourceException("این کتاب قبلاً پس داده شده");
        }

        borrow.setStatus(Status.RETURNED);
        borrow.setReturnTime(LocalDateTime.now());

        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        Borrow saved = borrowRepository.save(borrow);
        return mapToDto(saved);
    }

    // ۳. گرفتن امانت با id
    public BorrowResponseDto getBorrowById(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("امانتی با این id پیدا نشد"));
        return mapToDto(borrow);
    }

    // ۴. همه‌ی امانت‌های یه کاربر
    public Page<BorrowResponseDto> getBorrowsByUser(Long userId, Pageable pageable) {
        return borrowRepository.findByUserId(userId, pageable)
                .map(this::mapToDto);
    }

    // متد کمکی: تبدیل Entity به DTO
    private BorrowResponseDto mapToDto(Borrow borrow) {
        return BorrowResponseDto.builder()
                .id(borrow.getId())
                .traceCode(borrow.getTraceCode())
                .userId(borrow.getUser().getId())
                .username(borrow.getUser().getUsername())
                .bookId(borrow.getBook().getId())
                .bookName(borrow.getBook().getBookName())
                .deliverTime(borrow.getDeliverTime())
                .returnTime(borrow.getReturnTime())
                .status(borrow.getStatus())
                .build();
    }
}