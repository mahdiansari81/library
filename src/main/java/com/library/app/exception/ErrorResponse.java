package com.library.app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;              // کد وضعیت (404, 400, 409, 500)
    private String message;          // پیام خطا
    private String error;            // نوع خطا (Not Found, Bad Request, ...)
    private LocalDateTime timestamp; // زمان خطا
    private String path;             // مسیر درخواست
}