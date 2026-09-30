package com.library.app.dto;

import com.library.app.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowResponseDto {

    private Long id;
    private String traceCode;
    private Long userId;
    private String username;
    private Long bookId;
    private String bookName;
    private LocalDateTime deliverTime;
    private LocalDateTime returnTime;
    private Status status;
}