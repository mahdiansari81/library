package com.library.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRequestDto {

    @NotNull(message = "شناسه کاربر الزامی است")
    private Long userId;

    @NotNull(message = "شناسه کتاب الزامی است")
    private Long bookId;

    private Integer borrowDays;
}