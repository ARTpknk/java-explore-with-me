package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.model.category.Category;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=CategoryServiceTest",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryServiceTest {
    @Autowired
    CategoryService service;

    @Test
    void test() {
        Category category1 = new Category(1L, "Спорт");
        Category category2 = new Category(2L, "Кино");
        service.createCategory(category1);
        service.createCategory(category2);

        assertThat(category1, equalTo(service.getCategoryById(1L)));

        category1.setName("Концерт");
        assertThat(category1, equalTo(service.updateCategory(category1)));

        List<Category> list = new ArrayList<>();
        list.add(0, category1);
        list.add(1, category2);
        assertThat(list, equalTo(service.getCategories(0, 100)));

        list.remove(category1);
        service.deleteCategoryById(1L);
        assertThat(list, equalTo(service.getCategories(0, 100)));
    }
}