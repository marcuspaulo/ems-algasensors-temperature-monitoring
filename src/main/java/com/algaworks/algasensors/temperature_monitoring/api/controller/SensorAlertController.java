package com.algaworks.algasensors.temperature_monitoring.api.controller;

import com.algaworks.algasensors.temperature_monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature_monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature_monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature_monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature_monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
@Validated
public class SensorAlertController {
    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SensorAlertOutput getSensorAlert(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new SensorAlertOutput(
                sensorAlert.getId().getValue(),
                sensorAlert.getMaxTemperature(),
                sensorAlert.getMinTemperature()
        );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId, @RequestBody SensorAlertInput sensorAlertInput) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(new SensorAlert(new SensorId(sensorId), null, null));

        sensorAlert.setMinTemperature(sensorAlertInput.minTemperature());
        sensorAlert.setMaxTemperature(sensorAlertInput.maxTemperature());

        sensorAlertRepository.saveAndFlush(sensorAlert);

        return new SensorAlertOutput(
                sensorAlert.getId().getValue(),
                sensorAlert.getMaxTemperature(),
                sensorAlert.getMinTemperature()
        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensorAlertRepository.delete(sensorAlert);
    }
}
