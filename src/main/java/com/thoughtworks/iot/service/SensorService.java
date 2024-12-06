package com.thoughtworks.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.producers.KafkaSensorProducer;
import com.thoughtworks.iot.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("sensorService")
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;



    public List<Sensors> getSensors(){
        return sensorRepository.findAll();
    }

    public String  deleteSensor(Long id) throws SensorNotFoundException {

        Optional<Sensors> sensors = sensorRepository.findById(id);
        if (sensors.isPresent()) {
            sensorRepository.deleteById(id);
            return "Sensor deleted";
        } else {
            throw new SensorNotFoundException("Sensor not found");

        }
    }


    public Sensors create(Sensors sensor){

        if(sensor.getTemperature()==null || sensor.getTemperature() < -50 || sensor.getTemperature() > 150 ||  sensor.getSensorType()==null || sensor.getName()==null
        || sensor.getLatitude()==null || sensor.getLongitude()==null){

            throw new IllegalArgumentException("Sensor Properties are improper");
        }

        return sensorRepository.save(sensor);
    }



}
