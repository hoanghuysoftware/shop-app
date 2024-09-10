package org.family.hihishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.CategoryDTO;
import org.family.hihishop.model.Category;
import org.family.hihishop.services.CategoryService;
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
    private final CategoryService categoryService;
    private final ErrorMessage errorMessage;

    @GetMapping
    public ResponseEntity<?> doGetAll(
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> doGetById(@PathVariable Long id) {
        Category categories = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<?> doCreate(@Valid @RequestBody CategoryDTO categoryDTO,
                                      BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Successfully created !!!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> doUpdate(@PathVariable Long id,
                                           @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Successfully updated !!! " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Successfully deleted !!! ");
    }
}
