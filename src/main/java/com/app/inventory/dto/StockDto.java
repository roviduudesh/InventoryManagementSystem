package com.app.inventory.dto;

import com.app.inventory.dto.IdNameDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StockDto {

    private int id;
    private int itemId;
    private int supplierId;
    private double quantity;
    private String supplierName;
    private String itemName;
    private LocalDateTime stockDate;
}
