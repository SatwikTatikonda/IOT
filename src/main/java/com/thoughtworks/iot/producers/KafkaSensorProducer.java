package com.thoughtworks.iot.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.dtos.Temperature_TimeStamp_Binder;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.models.Sensors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KafkaSensorProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    public void sendData(Long id,List<Temperature_TimeStamp_Binder> dataObject) throws JsonProcessingException {


            kafkaTemplate.send("sensorData", String.valueOf(id),objectMapper.writeValueAsString(dataObject));

    }
}
