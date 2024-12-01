package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.SensorType;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.service.SensorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SensorControllerTest {


    @MockBean
    private SensorService sensorService;

    @Autowired
    private SensorController sensorController;

    @Autowired
    private  MockMvc mockMvc;



    @Test
    void getListofSensorsInDataBase() throws Exception {

        List<Sensors> mockList = Arrays.asList(
                new Sensors(1L, "AirSensor", SensorType.PROXIMITY, "WD1213", "TATA"),
                new Sensors(21L, "TouchSensor", SensorType.LIDAR, "TT1343", "TESLA")
        );

        when(sensorService.getSensors()).thenReturn(mockList);
        List<Sensors> actualList = sensorController.getAllSensors();
        assertEquals(actualList.size(), mockList.size());
        assertEquals(actualList, mockList);
//
//        mockMvc.perform(get("/api/get"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].name").value("AirSensor"));

    }

    @Test
    void deleteSensorById() throws Exception {

        when(sensorService.deleteSensor(101L)).thenReturn("Sensor deleted");
        String message = sensorController.deleteSensor(101L);
        assertEquals("Sensor deleted", message);
    }

    @Test
    void addSensorToDataBase() throws Exception {

        Sensors sensor=new Sensors(201L,"AirSensor", SensorType.PROXIMITY, "WD1213", "TATA");
        when(sensorService.create(sensor)).thenReturn(sensor);
        Sensors actualSensor = sensorController.addSensor(sensor);
        assertEquals(actualSensor, sensor);

    }

    @Test
    void deleteSensorFromDataBaseWhichIsNotPresent() throws Exception {
        Long id = -1L;
        when(sensorService.deleteSensor(id)).thenThrow(new SensorNotFoundException("Sensor not found!"));

        Exception exception = assertThrows(SensorNotFoundException.class, () -> {
            sensorController.deleteSensor(id);
        });

        assertEquals("Sensor not found!", exception.getMessage());
        verify(sensorService, times(1)).deleteSensor(id);
    }

    @Test
    void addSensorWithMissigValues() throws Exception {

        Sensors invalidSensor=new Sensors(9L,null,SensorType.LIDAR,"FF1234","STARLINK");
        when(sensorService.create(invalidSensor))
                .thenThrow(new IllegalArgumentException("Sensor Properties are improper"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sensorController.addSensor(invalidSensor);
        });

        assertEquals("Sensor Properties are improper", exception.getMessage());
        verify(sensorService, times(1)).create(invalidSensor);
    }

    @Test
    void addDuplicateSensor() {
        // Arrange
        Sensors sensor = new Sensors(1L, "AirSensor", SensorType.PROXIMITY, "WD1213", "TATA");
        when(sensorService.create(sensor)).thenThrow(new IllegalStateException("Sensor already exists!"));

        // Act & Assert
        try {
            sensorController.addSensor(sensor);
        } catch (IllegalStateException ex) {
            assertEquals("Sensor already exists!", ex.getMessage());
        }
        verify(sensorService, times(1)).create(sensor);
    }




}