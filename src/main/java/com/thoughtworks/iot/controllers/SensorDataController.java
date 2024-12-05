package com.thoughtworks.iot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.service.SensorDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/kafka")
    public String sendSensorData(@Valid @RequestBody SensorData sensorData){

        try{
            System.out.println("data to be sent to kafka");
            return objectMapper.writeValueAsString(sensorDataService.sendSensorData(sensorData));

        } catch (JsonProcessingException e) {
            return ("Error while sending sensor data to kafka");
        }

    }
}
