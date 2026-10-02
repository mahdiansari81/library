package com.library.app.service;

import com.library.app.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {


    public static Specification<Book> hasBookName(String bookName) {
        return (root, query, cb) -> {
            if (bookName == null || bookName.isBlank()) return null;
            return cb.like(cb.lower(root.get("bookName")), "%" + bookName.toLowerCase() + "%");
        };
    }


    public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) -> {
            if (author == null || author.isBlank()) return null;
            return cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
        };
    }


    public static Specification<Book> hasPublishYearBetween(Integer yearFrom, Integer yearTo) {
        return (root, query, cb) -> {
            if (yearFrom == null && yearTo == null) return null;
            if (yearFrom == null) return cb.lessThanOrEqualTo(root.get("publishYear"), yearTo);
            if (yearTo == null) return cb.greaterThanOrEqualTo(root.get("publishYear"), yearFrom);
            return cb.between(root.get("publishYear"), yearFrom, yearTo);
        };
    }


    public static Specification<Book> isAvailable() {
        return (root, query, cb) -> cb.greaterThan(root.get("availableCopies"), 0);
    }
}