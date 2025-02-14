package org.nurgisa.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class SensorDTO {
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 20, message = "Name size should be between 1 and 20")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Name should be in format: Sensor")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL) // Includes only if not null in request
    private LocalDateTime timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
