package ru.clevertec.newsManagement.util;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateStringConvert extends StdConverter<LocalTime, String> {

    public static final String DATA_FORMAT = "HH:mm:ss'Z'";

    @Override
    public String convert(LocalTime localDateTime) {
        return localDateTime == null ? "" : localDateTime
                .format(DateTimeFormatter
                        .ofPattern(DATA_FORMAT));

    }
}
