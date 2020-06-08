package com.rpo.backend.controllers;

import com.rpo.backend.models.Restaurant;
import com.rpo.backend.repositories.RestaurantRepository;
import com.rpo.backend.tools.DataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class RestaurantController {
    @Autowired
    RestaurantRepository restaurantRepository;

    // GET запрос, метод возвращает список ресторанов, который будет автоматически преобразован в JSON
    @GetMapping("/restaurants")
    public Page<Restaurant> getAllRestaurants(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return restaurantRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable(value = "id") Long shopId)
            throws DataValidationException
    {
        Restaurant restaurant = restaurantRepository.findById(shopId)
                .orElseThrow(()-> new DataValidationException("Ресторан с таким индеком не найден"));
        return ResponseEntity.ok(restaurant);
    }

    // POST запрос, сохраняем в таблице restaurants новый ресторан
    @PostMapping("/restaurants")
    public ResponseEntity<Object> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant nc = restaurantRepository.save(restaurant);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    // PUT запрос, обновляет запись в таблице restaurants
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если ресторан с указанным ключом отсутствует
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable(value = "id") Long restaurantId,
                                                 @Valid @RequestBody Restaurant restaurantDetails) {
        Restaurant restaurant = null;
        Optional<Restaurant> mm = restaurantRepository.findById(restaurantId);
        if (mm.isPresent())
        {
            restaurant = mm.get();
            restaurant.name = restaurantDetails.name;
            restaurant.location = restaurantDetails.location;
            restaurantRepository.save(restaurant);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "restaurant_not_found"
            );
        }
        return ResponseEntity.ok(restaurant);
    }

    // DELETE запрос, метод удаления записи из таблицы restaurants
    @PostMapping("/deleterestaurants")
    public ResponseEntity deleteRestaurants(@Valid @RequestBody List<Restaurant> restaurants) {
        restaurantRepository.deleteAll(restaurants);
        return new ResponseEntity(HttpStatus.OK);
    }
}

