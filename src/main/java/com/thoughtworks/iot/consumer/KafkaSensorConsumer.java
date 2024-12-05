package com.thoughtworks.iot.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class KafkaSensorConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @KafkaListener(topics = "sensorData",groupId = "sensor-data-consumer-group")
    public void listenToSensorData(String message) throws JsonProcessingException {

        System.out.println("here is the message "+ message);
        SensorData sensorData=objectMapper.readValue(message, SensorData.class);
        Long targetId=sensorData.getSensorId();
        // Fetch all sensor data from the last 5 minutes
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<SensorData> lastFiveMinutesData = sensorDataRepository
                .findByTimestampAfter(fiveMinutesAgo);

        System.out.println(targetId);
        System.out.println(lastFiveMinutesData);
        List<SensorData>targetData=new ArrayList<>();
        for(SensorData sensorDataOverLastFiveMinutes : lastFiveMinutesData) {

            if(sensorDataOverLastFiveMinutes.getSensorId()==targetId){

                targetData.add(sensorDataOverLastFiveMinutes);
            }
        }

        System.out.println(targetData);

        // Calculate and log the average temperature
        if (!targetData.isEmpty()) {
            double avgTemperature = targetData.stream()
                    .mapToDouble(SensorData::getTemperature)
                    .average()
                    .orElse(0.0);
            System.out.printf("Average Temperature (Last 5 Minutes): %.2f%n", avgTemperature);
        } else {
            System.out.println("No data available for the last 5 minutes.");
        }

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
