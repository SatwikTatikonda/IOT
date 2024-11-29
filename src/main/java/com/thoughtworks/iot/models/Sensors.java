package com.thoughtworks.iot.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sensorMap")
//@NoArgsConstructor
@AllArgsConstructor
public class Sensors {

    @Id
    @NotNull(message = "id cannot be null")
    private long id;
    @NotNull(message = "name cannot be null")
    private String name;
    private SensorType sensorType;
    @NotNull(message = "modelNumber cannot be null")
    private String modelNumber;
    @NotNull(message = "manufacturer cannot be null")
    private String manufacturer;



}
