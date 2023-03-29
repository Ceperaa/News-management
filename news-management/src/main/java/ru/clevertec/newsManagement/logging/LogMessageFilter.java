package ru.clevertec.newsManagement.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LogMessageFilter extends OncePerRequestFilter {

    @Getter
    private LogMessage logMessages;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logMessages = LogMessage.builder()
                .path( request.getRequestURL().toString())
                .httpMethod( request.getMethod())
                .build();
        filterChain.doFilter(request, response);
    }
}
