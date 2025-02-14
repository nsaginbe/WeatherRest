package org.nurgisa.weather.validators;

import org.nurgisa.weather.dto.MeasurementDTO;
import org.nurgisa.weather.dto.SensorDTO;
import org.nurgisa.weather.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;

        Double temperature = measurementDTO.getTemperature();
        if (temperature == null) {
            errors.rejectValue(
                    "temperature", "",
                    "Temperature cannot be null");
        }
        else if (temperature > 100 || temperature < -100) {
            errors.rejectValue(
                    "temperature", "",
                    "Temperature should be between -100 and 100");
        }

        if (measurementDTO.getRaining() == null) {
            errors.rejectValue(
                    "raining", "",
                    "Raining cannot be null");
        }

        SensorDTO sensorDTO = measurementDTO.getSensor();
        if (sensorDTO == null) {
            errors.rejectValue(
                    "sensor", "",
                    "Sensor cannot be null");
        }
        else if (sensorDTO.getName() != null && !sensorDTO.getName().isEmpty() &&
                sensorService.findByName(sensorDTO.getName()) == null) {
            errors.rejectValue(
                    "sensor", "",
                    "Sensor with name " + sensorDTO.getName() + " does not exist");
        }
    }
}
