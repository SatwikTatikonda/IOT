//package com.thoughtworks.iot.controllers;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.thoughtworks.iot.models.SensorData;
//import com.thoughtworks.iot.service.SensorDataService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.time.LocalDateTime;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class SensorDataControllerTest {
//
//    @MockBean
//    SensorDataService sensorDataService;
//
//    @MockBean
//    ObjectMapper objectMapper;
//
//    @Autowired
//    SensorDataController sensorDataController;
//
//    @Test
//    void sendSensorData() throws JsonProcessingException {
//
//        System.out.println("testing sesnorDataController");
//        SensorData sensorData = new SensorData();
//        sensorData.setSensorId(101L);
//
//        SensorData processedData = new SensorData();
//        processedData.setSensorId(101L);
//        processedData.setTimestamp(LocalDateTime.now());
//
//        when(sensorDataService.sendSensorData(sensorData)).thenReturn(processedData);
//        when(objectMapper.writeValueAsString(sensorData)).thenReturn("{\"sensorId\":\"sensor1\"}");
//        String message= sensorDataController.sendSensorData(sensorData);
//        assertNotNull(message);
//        assertEquals("{\"sensorId\":\"sensor1\"}", message);
//
//        verify(sensorDataService,times(1)).sendSensorData(sensorData);
//        verify(objectMapper,times(1)).writeValueAsString(processedData);
//
//    }
//}