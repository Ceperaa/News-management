package ru.clevertec.newsManagement.util;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringLocalDateConvert extends StdConverter<String, LocalTime> {
    @Override
    public LocalTime convert(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(s, DateTimeFormatter.ofPattern(LocalDateStringConvert.DATA_FORMAT));
    }
}
