package com.thoughtworks.iot.models;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public  class SensorData {

    @Id
    private String sensorDataId;
    private long sensorId;
    private double temperature;
    private LocalDateTime timestamp;
}