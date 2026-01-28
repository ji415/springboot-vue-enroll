package com.enroll.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return R.fail(msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleCV(ConstraintViolationException e) {
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntime(RuntimeException e) {
        return R.fail(e.getMessage());
    }
}
