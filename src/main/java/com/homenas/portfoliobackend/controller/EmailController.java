package com.homenas.portfoliobackend.controller;

import com.homenas.portfoliobackend.dto.EmailRequest;
import com.homenas.portfoliobackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.homenas.portfoliobackend.util.ValidationUtils.*;

@CrossOrigin("*")
@RequestMapping("/api/email")
@RestController
public class EmailController {

    private final EmailService emailService;
    private final ThreadPoolExecutor executor;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
        this.executor = new ThreadPoolExecutor(
                10, 10,
                5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    @PostMapping(
            value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        if (isValid(request, emailRequestValidator())) {
            AtomicReference<String> error = new AtomicReference<>("");
            executor.execute(() -> {
                try {
                    emailService.send(request.getEmail(), request.getFullName(), request.getMessage());
                } catch (MessagingException e) {
                    error.set(e.getMessage());
                }
            });
            if (!error.get().equals(""))
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.get());
            return ResponseEntity.ok(Collections.singletonMap("message", "Email has been sent"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }
}

