package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ExploreWithMeAlreadyExistsException;
import ru.practicum.exception.ExploreWithMeConflictException;
import ru.practicum.exception.ExploreWithMeNotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Category createCategory(Category category) {
        try {
            return repository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ExploreWithMeAlreadyExistsException(String.format(
                    "Category with name %s already exists", category.getName()));
        }
    }

    @Override
    @Transactional
    public Category getCategoryById(Long id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        } else {
            throw new ExploreWithMeNotFoundException("Category with Id: " + id + " not found");
        }
    }

    @Override
    @Transactional
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
    @Transactional
    public Category updateCategory(Category category) {
        Category oldCategory = getCategoryById(category.getId());

        if (!oldCategory.getName().equals(category.getName()) && repository.findByName(category.getName()) != null) {
            throw new ExploreWithMeAlreadyExistsException((String.format("Category with Name %s already exists",
                    category.getName())));
        }
        return repository.save(oldCategory.withName(category.getName()));
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return repository.findAll(PageRequest.of(from, size)).toList();
    }
}