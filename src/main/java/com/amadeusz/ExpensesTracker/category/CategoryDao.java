package com.amadeusz.ExpensesTracker.category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    void insertCategory(Category category);
    Optional<Category> selectCategoryByName(String name);
    boolean existsCategoryByName(String name);
    List<Category> getAllCategories();
}
