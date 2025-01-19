package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.EasterEggResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NumberFactServiceImpl implements NumberFactService{

    private static final String BASE_API_URL="http://numbersapi.com/";
    private final RestTemplate restTemplate;
    @Override
    public EasterEggResponse getNumberFact(int number) {
        String fact=restTemplate.getForObject(BASE_API_URL+number+"/trivia",String.class);

        return new EasterEggResponse("Great! You have found the hidden number fact ",fact);
    }
}
