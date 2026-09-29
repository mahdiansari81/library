package com.library.app.entity;

import com.library.app.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrows")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String traceCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Builder.Default
    private LocalDateTime deliverTime = LocalDateTime.now();

    private LocalDateTime returnTime;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status;
}