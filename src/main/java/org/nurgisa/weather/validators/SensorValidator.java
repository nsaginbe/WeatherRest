package org.nurgisa.weather.validators;

import org.nurgisa.weather.dto.SensorDTO;
import org.nurgisa.weather.models.Sensor;
import org.nurgisa.weather.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) target;

        sensorService.findAll().forEach(sensor -> {
            if (sensor.getName().equals(sensorDTO.getName())) {
                errors.rejectValue("name", "", "Sensor name already exists");
            }
        });
    }
}
