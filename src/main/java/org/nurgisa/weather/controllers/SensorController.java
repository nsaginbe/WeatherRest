package org.nurgisa.weather.controllers;

import jakarta.validation.Valid;
import org.nurgisa.weather.dto.SensorDTO;
import org.nurgisa.weather.models.Sensor;
import org.nurgisa.weather.services.SensorService;
import org.nurgisa.weather.util.ErrorResponse;
import org.nurgisa.weather.util.SensorNotRegisteredException;
import org.nurgisa.weather.validators.SensorValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;

    public SensorController(SensorService sensorService, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<SensorDTO> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                    BindingResult bindingResult) {

        sensorValidator.validate(sensorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new SensorNotRegisteredException(getErrorMessage(bindingResult));
        }

        sensorService.save(convertToSensor(sensorDTO));

        sensorDTO.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(sensorDTO, HttpStatus.CREATED); // 201
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(SensorNotRegisteredException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    private String getErrorMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();

        for (FieldError error : bindingResult.getFieldErrors()) {
            message.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        return message.toString();
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return new Sensor(sensorDTO.getName());
    }
}
