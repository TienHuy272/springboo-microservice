package com.hnt.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Response", description = "Schema to hold success response information")
public class ResponseDto {

    @Schema(description = "Status code of response", example = "200")
    private String statusCode;

    @Schema(description = "Status message in response ", example = "Request Process successfully")
    private String statusMsg;
}