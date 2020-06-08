package com.rpo.backend.repositories;

import com.rpo.backend.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

// MealRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы meals. Надо лишь указать тип Meal в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface MealRepository extends JpaRepository<Meal, Long> {
}
