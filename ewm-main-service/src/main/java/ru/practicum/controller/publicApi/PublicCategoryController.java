package ru.practicum.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exception.ExploreWithMeBadRequest;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.category.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                      @RequestParam(required = false, defaultValue = "10") int size) {
        if (from < 0 || size < 1) {
            throw new ExploreWithMeBadRequest("некорректные значения");
        }
        return categoryService.getCategories(from, size)
                .stream().map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{catId}")
    public CategoryDto findCategoryById(@PathVariable("catId") Long id) {
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(id));
    }
}