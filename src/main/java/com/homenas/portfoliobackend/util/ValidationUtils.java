package com.homenas.portfoliobackend.util;

import com.homenas.portfoliobackend.dto.EmailRequest;

import java.util.function.Function;

public class ValidationUtils {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

    public static <T> Boolean isValid(T t, Function<T, Boolean> validator) {
        return validator.apply(t);
    }

    public static Function<EmailRequest, Boolean> emailRequestValidator(){
        return emailRequest -> {
            boolean nv = emailRequest.getFullName().length() >= 2;
            boolean ev = emailRequest.getEmail().matches(EMAIL_REGEX);
            boolean mv = emailRequest.getMessage().length() > 25;

            return (nv && ev && mv);
        };
    }
}
