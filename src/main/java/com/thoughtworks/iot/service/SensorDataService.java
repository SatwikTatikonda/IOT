package com.thoughtworks.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.producers.KafkaSensorProducer;
import com.thoughtworks.iot.repository.SensorDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SensorDataService {

    private static final Logger logger = LoggerFactory.getLogger(SensorDataService.class);


    @Autowired
    private KafkaSensorProducer kafkaSensorProducer;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public SensorData sendSensorData(SensorData sensorData) throws JsonProcessingException {

        sensorData.setTimestamp(LocalDateTime.now());
        SensorData savedData= sensorDataRepository.save(sensorData);
        logger.info("Persisted sensor data: {}", savedData);
        kafkaSensorProducer.sendData(sensorData);
        return savedData;



    }
}
