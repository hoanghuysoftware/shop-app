package org.family.hihishop.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ErrorMessage {
    public List<String> getErrorMessages(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }
}
