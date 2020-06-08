package com.rpo.backend.repositories;

import com.rpo.backend.models.Cooker;
import org.springframework.data.jpa.repository.JpaRepository;

// CookerRepository - интерфейс, который по молчанию уже реализует
// базовый набор функций, позволяющий извлекать, модифицировать и удалять записи из
// таблицы cookers. Надо лишь указать тип Cooker в шаблоне JpaRepository. Когда нам
// понадобятся какие то особенные запросы к базе банных, мы сможем их добавить сюда.
public interface CookerRepository extends JpaRepository<Cooker, Long> {
}
