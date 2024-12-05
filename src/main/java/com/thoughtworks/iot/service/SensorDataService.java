package com.thoughtworks.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.iot.dtos.Temperature_TimeStamp_Binder;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.producers.KafkaSensorProducer;
import com.thoughtworks.iot.repository.SensorDataRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorDataService {



    @Autowired
    private KafkaSensorProducer kafkaSensorProducer;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public Map<Long, List<Temperature_TimeStamp_Binder>> temperatureMap=new HashMap<>();


    public void sendSensorData(Long id, Double temperature, LocalDateTime timestamp) throws JsonProcessingException {

//        System.out.println(temperature);
        kafkaSensorProducer.sendData(id,sensorDataRepository.saveSensorData(id,temperatureMap,new Temperature_TimeStamp_Binder(temperature,timestamp)).get(id));

    }
}
