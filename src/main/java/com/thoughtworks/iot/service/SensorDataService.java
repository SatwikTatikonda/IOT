package com.thoughtworks.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.producers.KafkaSensorProducer;
import com.thoughtworks.iot.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SensorDataService {



    @Autowired
    private KafkaSensorProducer kafkaSensorProducer;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public SensorData sendSensorData(SensorData sensorData) throws JsonProcessingException {

        sensorData.setTimestamp(LocalDateTime.now());
        kafkaSensorProducer.sendData(sensorData);
        return sensorDataRepository.save(sensorData);


    }
}
