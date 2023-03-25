package ru.clevertec.newsManagement.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogMessage {

    private String httpStatus;
    private String httpMethod;
    private String path;
    private String javaMethod;
    private String response;

}