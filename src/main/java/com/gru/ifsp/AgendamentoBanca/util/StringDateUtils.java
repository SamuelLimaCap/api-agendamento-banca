package com.gru.ifsp.AgendamentoBanca.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringDateUtils {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime parseString(String stringDateTime) {
            return LocalDateTime.parse(stringDateTime, formatter);
    }
}
