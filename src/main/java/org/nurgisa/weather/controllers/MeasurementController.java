package org.nurgisa.weather.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.nurgisa.weather.dto.MeasurementDTO;
import org.nurgisa.weather.models.Measurement;
import org.nurgisa.weather.models.Sensor;
import org.nurgisa.weather.services.MeasurementService;
import org.nurgisa.weather.services.SensorService;
import org.nurgisa.weather.util.ErrorResponse;
import org.nurgisa.weather.util.MeasurementNotAddedException;
import org.nurgisa.weather.validators.MeasurementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;
    private final SensorService sensorService;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeasurementValidator measurementValidator, ModelMapper modelMapper, SensorService sensorService) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
        this.sensorService = sensorService;
    }

    @GetMapping()
    public ResponseEntity<List<MeasurementDTO>> getMeasurements() {
        List<Measurement> measurements = measurementService.findAll();

        List<MeasurementDTO> measurementDTOs = measurements
                .stream()
                .map(this::convertToMeasurementDTO)
                .toList();

        return new ResponseEntity<>(measurementDTOs, HttpStatus.OK); // 200
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<List<MeasurementDTO>> getRainyDaysCount() {
        return ResponseEntity.ok(
                measurementService.findRainyDays()
                        .stream()
                        .map(this::convertToMeasurementDTO)
                        .toList()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<MeasurementDTO> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                 BindingResult bindingResult) {

        measurementValidator.validate(measurementDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new MeasurementNotAddedException(getErrorMessage(bindingResult));
        }

        measurementDTO.setTimestamp(LocalDateTime.now());
        measurementService.save(convertToMeasurement(measurementDTO));

        return new ResponseEntity<>(measurementDTO, HttpStatus.CREATED); // 201
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exceptionHandler(MeasurementNotAddedException ex) {
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

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);

        Sensor sensor = sensorService.findByName(measurementDTO.getSensor().getName());
        measurement.setSensor(sensor);

        return measurement;
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }
}
