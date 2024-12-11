package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping("/api/get")
    public List<Sensors> getAllSensors(){

        return sensorService.getSensors();
    }

    @DeleteMapping("/api/delete/{id}")
    public String deleteSensor(@PathVariable Long id) {

        try{
            return sensorService.deleteSensor(id);
        } catch (SensorNotFoundException e) {
            return e.getMessage();
        }

    }

    @PostMapping("/api/create")
    public Sensors addSensor(@Valid @RequestBody Sensors sensors){

//        System.out.println("sensor created "+sensors);
        return sensorService.create(sensors);
    }





}
