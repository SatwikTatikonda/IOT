package com.thoughtworks.iot.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.models.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaSensorProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSensorProducer.class);


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    public void sendData(SensorData sensorData) throws JsonProcessingException {

        logger.info("Sensor data sent to Kafka: {}", sensorData);

        kafkaTemplate.send("sensorData", String.valueOf(sensorData.getSensorId()),objectMapper.writeValueAsString(sensorData));

    }
}
