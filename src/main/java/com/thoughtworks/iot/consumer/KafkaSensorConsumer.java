package com.thoughtworks.iot.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.dtos.Temperature_TimeStamp_Binder;
import com.thoughtworks.iot.models.Sensors;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import java.util.ArrayList;

@Service
public class KafkaSensorConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<Long, List<Double>> sensorDataMap = new HashMap<>();

    @KafkaListener(topics = "sensorData",groupId = "sensor-data-consumer-group")
    public void listenToSensorData(String message) throws JsonProcessingException {

        System.out.println("here is the message "+ message);
        System.out.println("here is the object "+objectMapper.readValue(message, Temperature_TimeStamp_Binder.class));

    }

//    private void processSensorData(Sensors sensor) {
//
//        // Track temperature values for each sensor
//        sensorDataMap.putIfAbsent(sensor.getId(), new ArrayList<>());
//        List<Double> temperatures = sensorDataMap.get(sensor.getId());
//        temperatures.add(sensor.getTemperature());
//
//        // Retain data for the last 5 minutes
//        temperatures.removeIf(temp ->
//                sensor.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(5))
//        );
//
//        // Calculate average temperature
//        double averageTemperature = temperatures.stream()
//                .mapToDouble(Double::doubleValue)
//                .average()
//                .orElse(0.0);
//
//        // Generate alert if needed
//        if (averageTemperature > 40.0) {
//            generateAlert(sensorData, averageTemperature);
//        }
//    }
}
