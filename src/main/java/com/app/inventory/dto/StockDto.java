package com.app.inventory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StockDto {

//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    private Date stockDate;
    private double quantity;
    private int supplierId;
    private int itemId;
    private int userId;
}
