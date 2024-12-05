package com.thoughtworks.iot.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.models.SensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaSensorProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    public void sendData(SensorData sensorData) throws JsonProcessingException {


            kafkaTemplate.send("sensorData", String.valueOf(sensorData.getSensorId()),objectMapper.writeValueAsString(sensorData));

    }
}
