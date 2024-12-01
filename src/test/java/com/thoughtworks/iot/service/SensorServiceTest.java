package com.thoughtworks.iot.service;

import com.thoughtworks.iot.Exception.SensorNotFoundException;
import com.thoughtworks.iot.models.SensorType;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.repository.SensorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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


        List<Sensors> mockList=List.of(new Sensors(1L,"Air", SensorType.TEMPERATURE,"DD1234","MAHINDRA"),
                                        new Sensors(2L,"Water",SensorType.LIDAR,"ED1290","FORD"));

        when(sensorRepository.findAll()).thenReturn(mockList);

        List<Sensors>sensorsList=sensorService.getSensors();
        assertEquals(sensorsList,mockList);

    }

    @Test
    void deleteSensor() throws SensorNotFoundException {



        Long id=12L;
        when(sensorRepository.findById(id)).thenReturn(Optional.of(new Sensors(id, "AirSensor", SensorType.PROXIMITY, "WD1213", "TATA")));

        doNothing().when(sensorRepository).deleteById(id);
                String message="Sensor deleted";
                assertEquals(message,sensorService.deleteSensor(id));
    }

    @Test
    void testCreateSensor() {

        Sensors mockSensor=new Sensors(12L, "AirSensor", SensorType.PROXIMITY, "WD1213", "TATA");
        when(sensorRepository.save(mockSensor)).thenReturn(mockSensor);

        Sensors sensor=sensorService.create(mockSensor);
        assertEquals(sensor,mockSensor);

    }

    @Test
    void testCreateSensorWithInvalidSensorArguments(){
        Sensors mockSensor=new Sensors(-1L,null,null,null,null);

        Exception exception=assertThrows(IllegalArgumentException.class,()->sensorService.create(mockSensor));
        assertEquals("Invalid sensor arguments",exception.getMessage());

        verify(sensorRepository,never()).save(mockSensor);

    }



}