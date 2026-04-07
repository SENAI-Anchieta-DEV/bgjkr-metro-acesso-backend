package com.senai.bgjkr_metro_acesso_backend.interface_ui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ProblemDetailUtils {

    public static ProblemDetail buildProblem(HttpStatus status, String title, String detail, String path) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setInstance(URI.create(path));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
