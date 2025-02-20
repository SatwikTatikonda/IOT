package com.thoughtworks.iot.controllers;

import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.SensorType;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.service.SensorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;




import java.util.Arrays;
import java.util.Date;
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
                new Sensors(
                        1L,
                        "Temperature Sensor",
                        SensorType.TEMPERATURE,
                        "T12345",
                        "Acme Inc.",
                        25.5,
                        40.7128,
                        -74.0060,
                        new Date(),
                        new Date()
                ),
        new Sensors(
                2L,
                "Air Sensor",
                SensorType.LIDAR,
                "T12345",
                "Acme Inc.",
                25.5,
                40.7128,
                -74.0060,
                new Date(),
                new Date()
        )
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

        Sensors sensor=new Sensors(
                1L,
                "Temperature Sensor",
                SensorType.TEMPERATURE,
                "T12345",
                "Acme Inc.",
                25.5,
                40.7128,
                -74.0060,
                new Date(),
                new Date()
        );
        when(sensorService.create(sensor)).thenReturn(sensor);
        Sensors actualSensor = sensorController.addSensor(sensor);
        assertEquals(actualSensor, sensor);

    }

    @Test
    void deleteSensorFromDataBaseWhichIsNotPresent() throws SensorNotFoundException {

        Long id = -1L;
        when(sensorService.deleteSensor(id)).thenThrow(new SensorNotFoundException("Sensor not found!"));

        String message = sensorController.deleteSensor(id);

        assertEquals("Sensor not found!", message);
        verify(sensorService, times(1)).deleteSensor(id);
    }

    @Test
    void addSensorWithMissigValues() throws Exception {

        Sensors invalidSensor=new Sensors(
                1L,
                "Temperature Sensor",
                SensorType.TEMPERATURE,
                "T12345",
                "Acme Inc.",
                25.5,
                40.7128,
                -74.0060,
                new Date(),
                new Date()
        );
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
        Sensors sensor = new Sensors(
                1L,
                "Temperature Sensor",
                SensorType.TEMPERATURE,
                "T12345",
                "Acme Inc.",
                25.5,
                40.7128,
                -74.0060,
                new Date(),
                new Date()
        );
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