package org.nurgisa.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public class MeasurementDTO {
    private Double temperature; // Can be null in request

    private Boolean raining; // Can be null in request

    @Valid
    private SensorDTO sensor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timestamp;

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
