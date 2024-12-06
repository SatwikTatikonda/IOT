package com.thoughtworks.iot.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class KafkaSensorConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private KafkaTemplate kafkaTemplate;

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
            if(avgTemperature>40){
                sendAlerts("High Temperature Alert for Sensor "+String.valueOf(sensorData.getSensorId()));
            }

        } else {
            System.out.println("No sufficient Data available for the last 5 minutes.");
        }
    }


    public void sendAlerts(String message) throws JsonProcessingException {
        kafkaTemplate.send("sensor-alerts", message);
    }

}
