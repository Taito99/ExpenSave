package com.amadeusz.ExpensesTracker.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryDao categoryDao;

    public Optional<Category> getCategory(String name) {
        return categoryDao.selectCategoryByName(name);
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }
}
