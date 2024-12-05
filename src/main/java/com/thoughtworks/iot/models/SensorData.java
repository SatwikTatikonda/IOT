package com.thoughtworks.iot.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class SensorData {
    private long id;
    private double temperature;
    private LocalDateTime timestamp;
}