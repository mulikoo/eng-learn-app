package com.mulikoo.englearnapp.exceptions;

import com.mulikoo.englearnapp.dto.PersonExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionApiHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomAppException.class)
    public ResponseEntity<PersonExceptionDto> handleCustomAppException(CustomAppException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new PersonExceptionDto(ex.getMessage(), "CustomAppError"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PersonExceptionDto> handleGenericException(Exception ex, WebRequest request) {
        PersonExceptionDto errorDto = new PersonExceptionDto(
                "Внутренняя ошибка: " + ex.getMessage(),
                "InternalError"
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }


}
