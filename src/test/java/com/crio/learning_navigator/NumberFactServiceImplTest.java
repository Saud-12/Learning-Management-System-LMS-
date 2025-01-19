package com.crio.learning_navigator;

import com.crio.learning_navigator.dto.EasterEggResponse;
import com.crio.learning_navigator.service.NumberFactServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NumberFactServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NumberFactServiceImpl numberFactService;

    private static final String BASE_API_URL = "http://numbersapi.com/";

    @Test
    void getNumberFact_Success() {
        // Arrange
        int number = 42;
        String expectedResponse = "42 is the answer to life, the universe, and everything.";
        String expectedUrl = BASE_API_URL + number + "/trivia";

        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(expectedResponse);

        // Act
        EasterEggResponse response = numberFactService.getNumberFact(number);

        // Assert
        assertNotNull(response);
        assertEquals("Great! You have found the hidden number fact ", response.getMessage());
        assertEquals(expectedResponse, response.getResponse());
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }

    @Test
    void getNumberFact_WithZero() {
        // Arrange
        int number = 0;
        String expectedResponse = "0 is the additive identity.";
        String expectedUrl = BASE_API_URL + number + "/trivia";

        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(expectedResponse);

        // Act
        EasterEggResponse response = numberFactService.getNumberFact(number);

        // Assert
        assertNotNull(response);
        assertEquals("Great! You have found the hidden number fact ", response.getMessage());
        assertEquals(expectedResponse, response.getResponse());
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }

    @Test
    void getNumberFact_WithNegativeNumber() {
        // Arrange
        int number = -1;
        String expectedResponse = "-1 is the multiplicative identity for the ring of integers modulo 2.";
        String expectedUrl = BASE_API_URL + number + "/trivia";

        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(expectedResponse);

        // Act
        EasterEggResponse response = numberFactService.getNumberFact(number);

        // Assert
        assertNotNull(response);
        assertEquals("Great! You have found the hidden number fact ", response.getMessage());
        assertEquals(expectedResponse, response.getResponse());
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }

    @Test
    void getNumberFact_WhenAPIReturnsNull() {
        // Arrange
        int number = 42;
        String expectedUrl = BASE_API_URL + number + "/trivia";

        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(null);

        // Act
        EasterEggResponse response = numberFactService.getNumberFact(number);

        // Assert
        assertNotNull(response);
        assertEquals("Great! You have found the hidden number fact ", response.getMessage());
        assertNull(response.getResponse());
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }

    @Test
    void getNumberFact_WhenAPIThrowsException() {
        // Arrange
        int number = 42;
        String expectedUrl = BASE_API_URL + number + "/trivia";

        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenThrow(new RestClientException("API unavailable"));

        // Act & Assert
        assertThrows(RestClientException.class, () ->
                numberFactService.getNumberFact(number)
        );
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }

    @Test
    void getNumberFact_WithLargeNumber() {
        // Arrange
        int number = Integer.MAX_VALUE;
        String expectedResponse = "2147483647 is the maximum value for a 32-bit signed integer.";
        String expectedUrl = BASE_API_URL + number + "/trivia";

        when(restTemplate.getForObject(expectedUrl, String.class))
                .thenReturn(expectedResponse);

        // Act
        EasterEggResponse response = numberFactService.getNumberFact(number);

        // Assert
        assertNotNull(response);
        assertEquals("Great! You have found the hidden number fact ", response.getMessage());
        assertEquals(expectedResponse, response.getResponse());
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }
}