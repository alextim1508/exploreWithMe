package ru.practicum.explorewithme.statClient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.statClient.exception.HttpClientException;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface HttpClient {

    void hit(HttpServletRequest request) throws HttpClientException;

    List<StatDto> get(Map<String, String> parameters) throws JsonProcessingException, HttpClientException;
}
