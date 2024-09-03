package org.family.hihishop.services;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.CategoryDTO;
import org.family.hihishop.model.Category;
import org.family.hihishop.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) { // tren thuc te thi dungf dung thu vien(do day chi 1 attribute)
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findCategoryById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existing = categoryRepository.findCategoryById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
        existing.setName(categoryDTO.getName());
        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
