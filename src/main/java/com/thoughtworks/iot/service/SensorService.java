package com.thoughtworks.iot.service;

import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sensorService")
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public List<Sensors> getSensors(){
        return sensorRepository.findAll();
    }

    public String  deleteSensor(Long id) {

         sensorRepository.deleteById(id);
        System.out.println("sensor deleted");
         return "Sensor deleted";
    }

    public Sensors create(Sensors sensors) {
        return sensorRepository.save(sensors);
    }
}
