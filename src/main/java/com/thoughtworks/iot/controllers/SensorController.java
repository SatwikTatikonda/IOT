package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping("/sensors")
    public List<Sensors> getAllSensors(){

        return sensorService.getSensors();
    }

    @DeleteMapping("/sensors/{id}")
    public String deleteSensor(@PathVariable Long id){
        return sensorService.deleteSensor(id);
    }

    @PostMapping("/sensor")
    public Sensors addSensor(@RequestBody Sensors sensors){
        return sensorService.create(sensors);
    }



}
