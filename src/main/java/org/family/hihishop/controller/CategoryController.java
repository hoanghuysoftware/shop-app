package org.family.hihishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.CategoryDTO;
import org.family.hihishop.utils.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ErrorMessage errorMessage;
    @GetMapping
    public ResponseEntity<String> doGetAll(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("Successfully !!!" + " page: " + page + " limit: " + limit);
    }

    @PostMapping
    public ResponseEntity<?> doCreate(@Valid @RequestBody CategoryDTO categoryDTO,
                                           BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
        }
        return ResponseEntity.ok("Successfully created !!!" + categoryDTO.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> doUpdate(@PathVariable Long id) {
        return ResponseEntity.ok("Successfully updated !!! " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable Long id) {
        return ResponseEntity.ok("Successfully deleted !!! " + id);
    }
}
