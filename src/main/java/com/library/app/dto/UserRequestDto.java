package com.library.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "نام کاربری الزامی است")
    private String username;

    @NotBlank(message = "شماره موبایل الزامی است")
    private String mobile;

    @NotBlank(message = "رمز عبور الزامی است")
    private String password;
}