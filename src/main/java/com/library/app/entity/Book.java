package com.library.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bookName;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String bookCode;

    private Integer publishYear;

    @Column(nullable = false)
    private Integer totalCopies;      // تعداد کل

    @Column(nullable = false)
    private Integer availableCopies;  // تعداد موجود

    @Version
    private Long version;             // برای Concurrency
}
