package com.algaworks.algasensors.temperature_monitoring.api.model;

import io.hypersistence.tsid.TSID;

public record SensorAlertOutput(
        TSID id,
        Double maxTemperature,
        Double minTemperature
) {
}
