package com.app.inventory.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Common {

    public static final String SUCCESS = "Success";
    public static final String ERROR = "Error";
    public static final String EXCEPTION = "Technical Failure";

    public static String localDateTimeToDateWithSlash(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDateTime);
    }
}
