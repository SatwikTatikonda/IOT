package com.thoughtworks.iot.service;

import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Sensors create(Sensors sensors) {

        if(sensors.getSensorType()==null || sensors.getName()==null || sensors.getManufacturer()==null || sensors.getModelNumber()==null){
            throw new IllegalArgumentException("Sensor Properties are improper");
        }
        return sensorRepository.save(sensors);
    }
}
