package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    @Query(value = "select c from Category c " +
            "order by c.id")
    Page<Category> findAllOrderById(Pageable pageable);

}