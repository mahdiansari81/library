package com.library.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {

    private Long id;
    private String bookName;
    private String author;
    private String bookCode;
    private Integer publishYear;
    private Integer totalCopies;
    private Integer availableCopies;
}