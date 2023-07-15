package ru.practicum.service.category;

import ru.practicum.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);

    Category getCategoryById(Long id);

    void deleteCategoryById(Long id);

    Category updateCategory(Category category);

    List<Category> getCategories(int from, int size);
}