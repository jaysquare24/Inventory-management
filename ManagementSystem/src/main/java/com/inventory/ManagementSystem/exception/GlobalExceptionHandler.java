package com.inventory.ManagementSystem.exception;

import com.inventory.ManagementSystem.dto.CustomApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private CustomApiResponse<Void> errorMessage(Exception ex){
        return new CustomApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }

    private ResponseEntity<CustomApiResponse<Void>> errorResponse(
            HttpStatus status,
            String message
    ) {

        return ResponseEntity.status(status)
                .body(new CustomApiResponse<>(
                        false,
                        message,
                        null
                ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<CustomApiResponse<Void>>
    handleEmailExists(
            EmailAlreadyExistsException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        errorMessage(ex)
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomApiResponse<Void>>
    handleBadCredentials(
            BadCredentialsException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new CustomApiResponse<>(
                                false,
                                "Invalid email or password",
                                null
                        )
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        new CustomApiResponse<>(
                                false,
                                "You do not have permission to access this resource",
                                null
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<Void>>
    handleGeneralException(
            Exception ex
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new CustomApiResponse<>(
                                false,
                                "Something went wrong",
                                null
                        )
                );
    }


    @ExceptionHandler({ InvalidOtpException.class, OtpExpiredException.class})
    public ResponseEntity<CustomApiResponse<Void>> handleOtpExceptions(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        errorMessage(ex)
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public  ResponseEntity<CustomApiResponse<Void>> handleUserException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new CustomApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public  ResponseEntity<CustomApiResponse<Void>> handleIncorrectException(IncorrectPasswordException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new CustomApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleResourcesNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new CustomApiResponse<>(
                                false,
                                ex.getMessage(),
                                null
                        )
                );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleInsufficientResourcesException(InsufficientStockException ex){
        return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
