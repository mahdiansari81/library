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

    private String traceCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime deliverTime;

    private LocalDateTime returnTime;

    @Enumerated(EnumType.STRING)
    private Status status;
}