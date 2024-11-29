package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @GetMapping("/api/public")
    public List<Sensors> getAllSensors(){

        return sensorService.getSensors();
    }

    @DeleteMapping("/api/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSensor(@PathVariable Long id){

        System.out.println("calling delete");
        return sensorService.deleteSensor(id);
    }

    @PostMapping("/api/public")
    public Sensors addSensor(@Valid @RequestBody Sensors sensors){
        return sensorService.create(sensors);
    }



}
