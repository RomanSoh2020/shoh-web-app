package com.rpo.backend.controllers;

import com.rpo.backend.models.Cooker;
import com.rpo.backend.repositories.CookerRepository;
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
public class CookerController {
    @Autowired
    CookerRepository cookerRepository;

    // GET запрос, метод возвращает список поваров, который будет автоматически преобразован в JSON
    @GetMapping("/cookers")
    public Page<Cooker> getAllCookers(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return cookerRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/cookers/{id}")
    public ResponseEntity<Cooker> getCooker(@PathVariable(value = "id") Long cookerId)
            throws DataValidationException
    {
        Cooker cooker = cookerRepository.findById(cookerId)
                .orElseThrow(()-> new DataValidationException("Повар с таким индексом не найден"));
        return ResponseEntity.ok(cooker);
    }

    // POST запрос, сохраняем в таблице cookers нового повара
    @PostMapping("/cookers")
    public ResponseEntity<Object> createCooker(@Valid @RequestBody Cooker cooker) {
        Cooker nc = cookerRepository.save(cooker);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    // PUT запрос, обновляет запись в таблице cookers
    // Здесь обработка ошибки сводится к тому, что мы возвращаем код 404 на запрос в случае,
    // если повар с указанным ключом отсутствует
    @PutMapping("/artists/{id}")
    public ResponseEntity<Cooker> updateCooker(@PathVariable(value = "id") Long cookerId,
                                               @Valid @RequestBody Cooker cookerDetails) {
        Cooker cooker = null;
        Optional<Cooker> aa = cookerRepository.findById(cookerId);
        if (aa.isPresent())
        {
            cooker = aa.get();
            cooker.name = cookerDetails.name;
            cooker.rating = cookerDetails.rating;
            cookerRepository.save(cooker);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "cooker_not_found"
            );
        }
        return ResponseEntity.ok(cooker);
    }

    // Метод удаления записи из таблицы cookers
    @PostMapping("/deletecookers")
    public ResponseEntity deleteCookers(@Valid @RequestBody List<Cooker> cookers) {
        cookerRepository.deleteAll(cookers);
        return new ResponseEntity(HttpStatus.OK);
    }
}
