package com.equipo2.Integrador.exceptions;

import com.equipo2.Integrador.util.JsonResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<JsonResponseError> procesarExcepcionBadRequest(BadRequestException ex)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<JsonResponseError> procesarExcepcionResourceNotFound(ResourceNotFoundException ex)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponseError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<JsonResponseError> procesarExcepcionResourceConflict(ResourceConflictException ex)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponseError(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<JsonResponseError> procesarExcepcionUnauthorizedAccess(UnauthorizedAccessException ex)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonResponseError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(VerificationTokenException.class)
    public ResponseEntity<JsonResponseError> procesarExcepcionVerificationToken(VerificationTokenException ex)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JsonResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}