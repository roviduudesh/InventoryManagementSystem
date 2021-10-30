package com.app.inventory.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Common {
    public static String localDateTimeToDateWithSlash(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDateTime);
    }
}
