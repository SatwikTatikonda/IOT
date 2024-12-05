package com.thoughtworks.iot.service;

import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.SensorType;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SensorServiceTest {

    @Autowired
    SensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    @Test
    void getSensors() {


        List<Sensors> mockList=Arrays.asList(
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
        when(sensorRepository.findAll()).thenReturn(mockList);

        List<Sensors>sensorsList=sensorService.getSensors();
        assertEquals(sensorsList,mockList);

    }

    @Test
    void deleteSensor() throws SensorNotFoundException {



        Long id=12L;
        when(sensorRepository.findById(id)).thenReturn(Optional.of(new Sensors(
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
        )));

        doNothing().when(sensorRepository).deleteById(id);
                String message="Sensor deleted";
                assertEquals(message,sensorService.deleteSensor(id));
    }

    @Test
    void testCreateSensor() {

        Sensors mockSensor=new Sensors(
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
        when(sensorRepository.save(mockSensor)).thenReturn(mockSensor);

        Sensors sensor=sensorService.create(mockSensor);
        assertEquals(sensor,mockSensor);

    }

    @Test
    void testCreateSensorWithInvalidSensorArguments(){
        Sensors mockSensor=new Sensors(
                1L,
                null,
                SensorType.TEMPERATURE,
                "T12345",
                "Acme Inc.",
                null,
                null,
                -74.0060,
                new Date(),
                new Date()
        );;

        Exception exception=assertThrows(IllegalArgumentException.class,()->sensorService.create(mockSensor));
        assertEquals("Sensor Properties are improper",exception.getMessage());

        verify(sensorRepository,never()).save(mockSensor);

    }


    @Test
    void testGetSensorsReturnsEmptyList() {
        when(sensorRepository.findAll()).thenReturn(Arrays.asList());

        List<Sensors> sensors = sensorService.getSensors();

        assertTrue(sensors.isEmpty());
        verify(sensorRepository, times(1)).findAll();
    }
    @Test
    void testDeleteSensorThrowsException() {
        Long sensorId = 1L;

        when(sensorRepository.findById(sensorId)).thenReturn(Optional.empty());

        assertThrows(SensorNotFoundException.class, () -> sensorService.deleteSensor(sensorId));
        verify(sensorRepository, times(1)).findById(sensorId);
        verify(sensorRepository, never()).deleteById(sensorId);
    }

    @Test
    void testCreateSensorThrowsExceptionForNullProperties() {
        Sensors invalidSensor = new Sensors(
                1L,
null,                SensorType.TEMPERATURE,
                "T12345",
                "Acme Inc.",
                null,
                40.7128,
                -74.0060,
                new Date(),
                new Date()
        );;

        assertThrows(IllegalArgumentException.class, () -> sensorService.create(invalidSensor));
        verify(sensorRepository, never()).save(invalidSensor);
    }

    @Test
    void testCreateSensorValidProperties() {
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

        when(sensorRepository.save(sensor)).thenReturn(sensor);

        Sensors savedSensor = sensorService.create(sensor);

        assertNotNull(savedSensor);
        assertEquals("Temperature Sensor", savedSensor.getName());
        verify(sensorRepository, times(1)).save(sensor);
    }









}