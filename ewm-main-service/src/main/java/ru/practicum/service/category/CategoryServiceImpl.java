package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Override
    public Category createCategory(Category category) {
        if (repository.findByName(category.getName()) != null) {
            throw new ExploreWithMeConflictException((String.format("Category with Name %s already exists",
                    category.getName())));
        }
        return repository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new ExploreWithMeNotFoundException("Category with Id: " + id + " not found");
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        //только если нет событий по категории
        //добавить проверку
        if (getCategoryById(id) == null) {
            throw new ExploreWithMeNotFoundException("Category with Id: " + id + " not found");
        }
        if (!eventRepository.findAllByCategoryId(id).isEmpty()) {
            throw new ExploreWithMeConflictException("Category with Id: " + id + " has events");
        }
        repository.deleteById(id);
    }

    @Override
    public Category updateCategory(Category category) {
        Category oldCategory = getCategoryById(category.getId());
        if (!oldCategory.getName().equals(category.getName()) && repository.findByName(category.getName()) != null) {
            throw new ExploreWithMeConflictException((String.format("Category with Name %s already exists",
                    category.getName())));
        }
        oldCategory.setName(category.getName());
        return repository.save(oldCategory);
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return repository.findAll(PageRequest.of(from, size)).toList();
    }
}