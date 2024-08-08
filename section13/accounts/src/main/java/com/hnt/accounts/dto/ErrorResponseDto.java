package com.hnt.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(name = "Error response", description = "Hold error response information")
public class ErrorResponseDto {
    @Schema(name = "apiPath...", description = "API call by client")
    private String apiPath;

    @Schema(description = "Error response code happened")
    private HttpStatus errorCode;

    @Schema(description = "Error message representing the error happened")
    private String errorMessage;

    @Schema(description = "Time when error happened")
    private LocalDateTime errorTime;
}
