package com.mulikoo.englearnapp.exceptions;

import com.mulikoo.englearnapp.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String errorString = errors.stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDto exceptionDto = new ErrorResponseDto(errorString, ex.getClass().getSimpleName());
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(exceptionDto, status);
    }

    @ExceptionHandler(CustomAppException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomAppException(CustomAppException ex, WebRequest request) {
        ErrorResponseDto exceptionDto = new ErrorResponseDto(
                ex.getMessage(),
                ex.getClass().getSimpleName()
        );
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                ex.getMessage(),
                ex.getClass().getSimpleName()
        );
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthException(AuthorizationDeniedException ex, WebRequest request) {
        ErrorResponseDto errorDto = new ErrorResponseDto(
                ex.getMessage(),
                ex.getClass().getSimpleName()
        );
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorDto);
    }

}


