package com.amadeusz.ExpensesTracker.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CategoryDataAccessService implements CategoryDao{
    private final CategoryRepository categoryRepository;

}
