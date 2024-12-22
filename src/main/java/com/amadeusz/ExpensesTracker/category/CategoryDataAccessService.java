package com.amadeusz.ExpensesTracker.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CategoryDataAccessService implements CategoryDao{
    private final CategoryRepository categoryRepository;

    @Override
    public void insertCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Optional<Category> selectCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public boolean existsCategoryByName(String name) {
        return categoryRepository.existsCategoryByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
