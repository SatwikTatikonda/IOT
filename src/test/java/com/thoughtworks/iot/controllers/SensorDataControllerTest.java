package com.thoughtworks.iot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.service.SensorDataService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SensorDataControllerTest {


//    @MockBean
//    SensorDataService sensorDataService;
//
//    @MockBean
//    ObjectMapper objectMapper;
//
//    @Autowired
//    SensorDataController sensorDataController;



//    @Test
//    void sendSensorData() throws JsonProcessingException, org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException {
//
//        System.out.println("testing sesnorDataController");
//        SensorData sensorData = new SensorData();
//        sensorData.setSensorId(101L);
//        sensorData.setTemperature(32.6);
//
////        System.out.println(sensorData);
//        SensorData processedData = new SensorData();
//        processedData.setSensorId(101L);
//        processedData.setTemperature(32.6);
//        processedData.setTimestamp(LocalDateTime.now());
//
////        System.out.println(processedData);
//
//        when(sensorDataService.sendSensorData(sensorData)).thenReturn(processedData);
//        when(objectMapper.writeValueAsString(any(SensorData.class))).thenReturn("{\"sensorId\":\"sensor1\"}");
//        String message= sensorDataController.sendSensorData(sensorData);
//        System.out.println(message);
//        assertNotNull(message);
//        assertEquals("{\"sensorId\":\"sensor1\"}", message);
//
//        verify(sensorDataService,times(1)).sendSensorData(sensorData);
//        verify(objectMapper,times(1)).writeValueAsString(processedData);
//
//    }


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorDataService sensorDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendSensorData() throws Exception {
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(101L);
        sensorData.setTemperature(32.6);

        SensorData processedData = new SensorData();
        processedData.setSensorId(101L);
        processedData.setTemperature(32.6);
        processedData.setTimestamp(LocalDateTime.now());

        when(sensorDataService.sendSensorData(sensorData)).thenReturn(processedData);

        mockMvc.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorData))) // Pass input JSON
                .andExpect(status().isOk()); // Check for 200 OK status

        verify(sensorDataService, times(1)).sendSensorData(sensorData);
    }




}