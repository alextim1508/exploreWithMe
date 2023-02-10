package ru.practicum.explorewithme.statClient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.statClient.exception.HttpClientException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StatServerClient implements HttpClient {

    public static final long DEFAULT_PERIOD_IN_DAYS = 31;

    public final DateTimeFormatter formatter;

    private final String getPath;

    private final String postPath;

    private final String appName;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;


    @Autowired
    public StatServerClient(@Value("${app.name}") String appName,
                            @Value("${stat-server.url}") String baseUrl,
                            @Value("${stat-server.getPath}") String getPath,
                            @Value("${stat-server.postPath}") String postPath,
                            @Value("${spring.mvc.format.date-time}") String dateTimeFormat,
                            RestTemplateBuilder restTemplateBuilder,
                            ObjectMapper objectMapper) {

        this.restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(baseUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory.class)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();

        this.appName = appName;
        this.getPath = getPath;
        this.postPath = postPath;

        this.objectMapper = objectMapper;
        this.formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    @Override
    public void hit(HttpServletRequest request) throws HttpClientException {
        HttpEntity<HitDto> requestBody = new HttpEntity<>(
                HitDto.builder()
                        .app(appName)
                        .uri(request.getRequestURI())
                        .ip(request.getRemoteAddr())
                        .timestamp(LocalDateTime.now()).build()
        );

        ResponseEntity<String> response = restTemplate.postForEntity(postPath, requestBody, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new HttpClientException(String.format("Stat server response code is %d, error: %s",
                    response.getStatusCode().value(), response.getBody()));
        }
    }

    @Override
    public List<StatDto> get(Map<String, String> parameters) throws JsonProcessingException, HttpClientException {
        if (parameters == null) {
            parameters = new HashMap<>();
        }

        if (!parameters.containsKey("start")) {
            String startTime = LocalDateTime.now().minusDays(DEFAULT_PERIOD_IN_DAYS).format(formatter);
            parameters.put("start", startTime);
        }

        if (!parameters.containsKey("end")) {
            String endTime = LocalDateTime.now().format(formatter);
            parameters.put("end", endTime);
        }

        ResponseEntity<String> response = restTemplate.getForEntity(getUriWithParams(getPath, parameters), String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String body = response.getBody();
            return objectMapper.readValue(body, new TypeReference<>() {
            });
        }

        throw new HttpClientException(String.format("Stat server response code is %d, error: %s",
                response.getStatusCode().value(), response.getBody()));
    }

    private String getUriWithParams(String uri, Map<String, String> params) {
        if (params == null)
            return uri;

        StringBuilder builder = new StringBuilder(uri);
        builder.append("?");
        params.forEach((k, v) -> {
            builder.append(k).append("=").append(v).append("&");
        });

        return builder.substring(0, builder.length() - 1);
    }
}
