package com.thoughtworks.iot.models;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sensorMap")
public class Sensors {

    @Id
    private long id;
    private String name;
    private SensorType sensorType;
    private String modelNumber;
    private String manufacturer;

}
