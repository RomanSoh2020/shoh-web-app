package com.rpo.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity // указывает, что класс - это таблица в базе
@Table(name = "restaurants") // указывается имя таблицы
@Access(AccessType.FIELD) //указывает на то, что мы разрешаем доступ к полям класса
public class Restaurant {     //вместо того, чтобы для каждого поля делать методы чтения и записи

    public Restaurant() { }
    public Restaurant(Long id) {
        this.id = id;
    }

    @Id //Каждое поле класса связывается с полем в таблице а для поля ключа, дополнительно, указываются его свойства.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false, unique = true)
    public String name;

    @Column(name = "location", nullable = false)
    public String location;

    @JsonIgnore
    @OneToMany
    public List<Meal> meals = new ArrayList<>();

    // В настройках отношения @ManyToMany указывается что она сделана через таблицу
    // usersrestaurants по ее полям restid и userid. Со стороны таблицы restaurants это
    // joinColumns, а со стороны users inverseJoinColums. В модели Users должно быть добавлено
    // такое же определение, но в обратном порядке. Однако без этого можно обойтись,
    // воспользовавшись атрибутом mappedBy. Тогда компилятор просто возьмет нужную
    // информацию из аннотации поля Restaurant.users.
    // ***
    // В отношении многие-ко-многим есть та проблема циклических ссылок.
    // Разрываем цепочку с помощью аннотации @JsonIgnore для поля Restaurant.users.
    // ***
    // Поля участвующие в отношении многие-ко-многим вместо
    // типа List (список) имеют тип Set (множество). Для типа List JPA
    // генерирует очень неэффективный код запросов к базе данных.
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "usersrestaurants",
            joinColumns = @JoinColumn(name = "restid"),
            inverseJoinColumns = @JoinColumn(name = "userid"))
    public Set<User> users = new HashSet<>();
}
