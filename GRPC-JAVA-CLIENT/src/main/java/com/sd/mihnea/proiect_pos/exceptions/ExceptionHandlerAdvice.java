package com.sd.mihnea.proiect_pos.exceptions;


import com.sd.mihnea.proiect_pos.DTOs.ErrorDTO;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;


import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerAdvice{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> _422_UnprocessableContent_Handler(MethodArgumentNotValidException exception, HttpServletRequest request)
    {

        StringBuilder stringBuilder = new StringBuilder();


        for(FieldError error : exception.getBindingResult().getFieldErrors())
        {
            stringBuilder.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(),HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), stringBuilder.toString(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorDTO);
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorDTO> _415_UnsupportedMediaType(HttpMediaTypeNotSupportedException exception, HttpServletRequest httpServletRequest)
    {

        ErrorDTO errorDto = new ErrorDTO(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), "Content-type must be application/json", httpServletRequest.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorDto);


    }




    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDTO> _404_ResourceNOTFOUND(NoSuchElementException ex, HttpServletRequest request)
    {

        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "Resource Does Not Exist", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> _409_ConflictConstraint(ConstraintViolationException ex, HttpServletRequest request)
    {


        StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            sb.append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
        }

        ErrorDTO errorDTO = new ErrorDTO(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                sb.toString(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);

    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorDTO> _403_Forbidden_Handler(Exception ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.FORBIDDEN;
        String message = "Access Denied";


        ErrorDTO errorDTO = new ErrorDTO(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorDTO);
    }


    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorDTO> _401_Unauthorized_Handler(Exception ex, HttpServletRequest request) {

        String message = "Authentication Failed";



        ErrorDTO errorDTO = new ErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }


}
