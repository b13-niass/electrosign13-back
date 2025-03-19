package com.codev13.electrosign13back.exception;

import com.core.communs.web.exceptions.CustomRuntimeException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessages.toString());
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleUserExceptions(CustomRuntimeException ex) {
        return buildErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public ResponseEntity<Map<String, Object>> handleFeignUnauthorizedException(FeignException.Unauthorized ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Échec de l'authentification. Vérifiez vos identifiants.");
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(FeignException ex) {
       try {
           String responseBody = ex.contentUTF8();
           JsonNode jsonNode = objectMapper.readTree(responseBody);
           String errorMessage = jsonNode.get("errorMessage").asText();
           return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
       }catch (Exception e){
           return buildErrorResponse(HttpStatus.resolve(ex.status()), "An unexpected error occurred");
       }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleUserExceptions(ValidationException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return  buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, "Fichier trop volumineux. Taille maximale autorisée: 10MB.");
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        String message = "An unexpected error occurred";
//        System.out.println(ex.getMessage());
//        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
//            status = ex.getClass().getAnnotation(ResponseStatus.class).value();
//            message = ex.getMessage();
//        }
//        else if (ex instanceof ResponseStatusException responseEx) {
//            status = (HttpStatus) responseEx.getStatusCode();
//            message = responseEx.getReason();
//        }
//        return buildErrorResponse(HttpStatus.resolve(status.value()), message);
//    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "KO");
        errorResponse.put("data", null);
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}
