package com.rpo.backend.controllers;

import com.rpo.backend.models.Meal;
import com.rpo.backend.repositories.MealRepository;
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
public class MealController {
    @Autowired
    MealRepository mealRepository;

    @GetMapping("/meals")
    public Page<Meal> getAllMeals(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return mealRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/meals/{id}")
    public ResponseEntity<Meal> getMeal(@PathVariable(value = "id") Long songId)
            throws DataValidationException
    {
        Meal painting = mealRepository.findById(songId)
                .orElseThrow(()-> new DataValidationException("Блюдо с таким индексом не найдено"));
        return ResponseEntity.ok(painting);
    }

    @PostMapping("/meals")
    public ResponseEntity<Object> createMeal(@Valid @RequestBody Meal meal) {
        Meal nc = mealRepository.save(meal);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    @PutMapping("/meals/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable(value = "id") Long mealId,
                                           @Valid @RequestBody Meal mealDetails) {
        Meal meal = null;
        Optional<Meal> mm = mealRepository.findById(mealId);
        if (mm.isPresent())
        {
            meal = mm.get();
            meal.name = mealDetails.name;
            mealRepository.save(meal);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "meal_not_found"
            );
        }
        return ResponseEntity.ok(meal);
    }

    @PostMapping("/deletemeals")
    public ResponseEntity deleteMeals(@Valid @RequestBody List<Meal> meals) {
        mealRepository.deleteAll(meals);
        return new ResponseEntity(HttpStatus.OK);
    }
}
