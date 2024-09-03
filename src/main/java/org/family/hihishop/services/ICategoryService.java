package org.family.hihishop.services;

import org.family.hihishop.dto.CategoryDTO;
import org.family.hihishop.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
