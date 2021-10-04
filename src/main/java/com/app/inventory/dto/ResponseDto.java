package com.app.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDto {
    private int status;
    private String message;
    private Object data;
}
