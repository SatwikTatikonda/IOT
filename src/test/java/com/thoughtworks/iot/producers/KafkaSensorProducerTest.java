package com.thoughtworks.iot.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.consumer.KafkaSensorConsumer;
import com.thoughtworks.iot.models.SensorData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
class KafkaSensorProducerTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KafkaSensorConsumer kafkaSensorConsumer;

    @Test
    void sendData() throws JsonProcessingException {

        SensorData sensorData=new SensorData();
        sensorData.setSensorId(101L);
        sensorData.setTemperature(33.6);

        String message=objectMapper.writeValueAsString(sensorData);

        doNothing().when(kafkaSensorConsumer).listenToSensorData(message);
        kafkaTemplate.send("sensorData", String.valueOf(sensorData.getSensorId()),objectMapper.writeValueAsString(sensorData));

    }
}