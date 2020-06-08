package com.rpo.backend.repositories;

import com.rpo.backend.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

// RestaurantRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы restaurants. Надо лишь указать тип Restaurant в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
