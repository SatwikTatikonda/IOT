package com.thoughtworks.iot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.repository.SensorDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class SensorDataServiceTest {

    @Autowired
    SensorDataService sensorDataService;

    @MockBean
    SensorDataRepository sensorDataRepository;

    @Test
    void sendSensorData() throws JsonProcessingException {


        SensorData sensorData = new SensorData();
        sensorData.setTemperature(34);
        sensorData.setTimestamp(LocalDateTime.now());
        sensorData.setSensorId(101);

        when(sensorDataRepository.save(sensorData)).thenReturn(sensorData);
        SensorData savedSenorData=sensorDataService.sendSensorData(sensorData);
        assertEquals(sensorData,savedSenorData);

    }
}