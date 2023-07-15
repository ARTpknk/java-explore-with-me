package ru.practicum.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Data
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @With
    @Column(name = "categories_name", nullable = false)
    String name;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}