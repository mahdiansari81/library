package com.library.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {

    @NotBlank(message = "عنوان کتاب الزامی است")
    private String bookName;

    @NotBlank(message = "نام نویسنده الزامی است")
    private String author;

    @NotBlank(message = "شابک الزامی است")
    private String bookCode;

    private Integer publishYear;

    @NotNull(message = "تعداد کل نسخه‌ها الزامی است")
    @Positive(message = "تعداد کل نسخه‌ها باید مثبت باشد")
    private Integer totalCopies;
}