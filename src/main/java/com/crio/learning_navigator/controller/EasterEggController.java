package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.EasterEggResponse;
import com.crio.learning_navigator.service.NumberFactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/easter-egg")
@RequiredArgsConstructor
public class EasterEggController {

    private final NumberFactService numberFactService;

    @GetMapping("/hidden-feature/{number}")
    public ResponseEntity<EasterEggResponse> getNuberFact(@PathVariable int number){
        return ResponseEntity.ok(numberFactService.getNumberFact(number));
    }
}
