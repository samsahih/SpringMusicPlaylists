package com.musicplaylists.demo.musicplaylists.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.function.Supplier;


@Service
public class ValidationUtils {

    public static ResponseEntity<?> handleValidationResult(BindingResult bindingResult, Supplier<ResponseEntity<?>> action, ProfileUtils profileUtils) {
        if (bindingResult.hasErrors()) {
            // Return validation errors if any
            return getErrorResponse(bindingResult, profileUtils);
        }
        else {
            // No errors, execute the action
            return action.get();
        }
    }

    private static ResponseEntity<?> getErrorResponse(BindingResult bindingResult, ProfileUtils profileUtils) {
        if (profileUtils.isLocalProfile()) {
            // Return detailed information if it's a local environment
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        } else {
            // Otherwise, return only error messages
            StringBuilder errorMessageBuilder = new StringBuilder();
            bindingResult.getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errorMessageBuilder.append(fieldName).append(": ").append(errorMessage).append("\n");
            });
            return ResponseEntity.badRequest().body(errorMessageBuilder);
        }
    }
}