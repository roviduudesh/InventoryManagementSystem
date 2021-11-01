package com.app.inventory.dto;

import lombok.Data;

@Data
public class ValidateDto {
    private boolean isValid;
    private String message;
}
