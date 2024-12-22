package com.amadeusz.ExpensesTracker.category;

import java.util.UUID;

public record CategoryDto(UUID id, String name, String icon) {
    public static CategoryDto fromCategory(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getIcon());
    }
}
