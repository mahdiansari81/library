package com.library.app.service;

import com.library.app.dto.BookRequestDto;
import com.library.app.dto.BookResponseDto;
import com.library.app.entity.Book;
import com.library.app.exception.DuplicateResourceException;
import com.library.app.exception.ResourceNotFoundException;
import com.library.app.repository.BookRepository;
import com.library.app.repository.BorrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;

    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        bookRepository.findByBookCode(dto.getBookCode())
                .ifPresent(b -> {
                    throw new DuplicateResourceException("کتابی با این شابک قبلاً ثبت شده");
                });

        Book book = Book.builder()
                .bookName(dto.getBookName())
                .author(dto.getAuthor())
                .bookCode(dto.getBookCode())
                .publishYear(dto.getPublishYear())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getTotalCopies())
                .build();

        Book saved = bookRepository.save(book);
        return mapToDto(saved);
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("کتابی با این id پیدا نشد"));
        return mapToDto(book);
    }

    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::mapToDto);
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("کتابی با این id پیدا نشد"));

        book.setBookName(dto.getBookName());
        book.setAuthor(dto.getAuthor());
        book.setBookCode(dto.getBookCode());
        book.setPublishYear(dto.getPublishYear());

        int diff = dto.getTotalCopies() - book.getTotalCopies();
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(book.getAvailableCopies() + diff);

        Book saved = bookRepository.save(book);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("کتابی با این id پیدا نشد"));

        boolean isBorrowed = !borrowRepository.findByBookId(id).isEmpty();
        if (isBorrowed) {
            throw new DuplicateResourceException("این کتاب در حال امانت است و نمی‌توان حذف کرد");
        }

        bookRepository.delete(book);
    }

    public Page<BookResponseDto> searchBooks(
            String bookName,
            String author,
            Integer yearFrom,
            Integer yearTo,
            Boolean available,
            Pageable pageable) {

        Specification<Book> spec = Specification.allOf();

        if (bookName != null && !bookName.isBlank()) {
            spec = spec.and(BookSpecification.hasBookName(bookName));
        }
        if (author != null && !author.isBlank()) {
            spec = spec.and(BookSpecification.hasAuthor(author));
        }
        if (yearFrom != null || yearTo != null) {
            spec = spec.and(BookSpecification.hasPublishYearBetween(yearFrom, yearTo));
        }
        if (Boolean.TRUE.equals(available)) {
            spec = spec.and(BookSpecification.isAvailable());
        }

        return bookRepository.findAll(spec, pageable).map(this::mapToDto);
    }

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