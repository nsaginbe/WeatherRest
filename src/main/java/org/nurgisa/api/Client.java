package org.nurgisa.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.nurgisa.api.graphics.XChart;
import org.nurgisa.weather.models.Measurement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Client {

    private static final String BASE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws JsonProcessingException {

//        registerSensor("Wisdom");

//        sendRandomMeasurements(100);

//        getMeasurements();

//        getRainyDays();

        XChart.plotChart(fetchMeasurements(getMeasurements()));
    }

    private static List<Measurement> fetchMeasurements(String query) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(query, new TypeReference<List<Measurement>>() {});
    }

    private static void registerSensor(String name) {
        String url = BASE_URL + "/sensors/registration";
        Map<String, String> requestBody = Map.of("name", name);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    private static void sendRandomMeasurements(int count) {
        String url = BASE_URL + "/measurements/add";
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            Map<String, Object> measurements = new HashMap<>();

            measurements.put("temperature", -100 + (200 * random.nextDouble()));
            measurements.put("raining", random.nextBoolean());
            measurements.put("sensor", Map.of("name", "Alpha"));

            ResponseEntity<String> response = restTemplate.postForEntity(url, measurements, String.class);
            System.out.println("Sent measurement " + (i + 1) + ": " + response.getStatusCode());
        }
    }

    private static String getMeasurements() {
        String url = BASE_URL + "/measurements";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }

    private static String getRainyDays() {
        String url = BASE_URL + "/measurements/rainyDaysCount";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }
}
