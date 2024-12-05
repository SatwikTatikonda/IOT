package com.thoughtworks.iot.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "sensorMap")
@NoArgsConstructor
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
    @NotNull(message = "Temperature is required")
    private Double temperature;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;


}
