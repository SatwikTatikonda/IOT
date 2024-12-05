package com.thoughtworks.iot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @PostMapping("/kafka")
    public void sendSensorData(@Valid @RequestBody SensorData sensorData){

        System.out.println("data to be sent to kafka "+sensorData);
        try{
            sensorDataService.sendSensorData(sensorData.getId(),sensorData.getTemperature(), LocalDateTime.now());
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending sensor data to kafka");
        }


    }
}
