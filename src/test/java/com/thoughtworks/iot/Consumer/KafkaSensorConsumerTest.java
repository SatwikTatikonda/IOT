package com.thoughtworks.iot.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.consumer.KafkaSensorConsumer;
import com.thoughtworks.iot.models.SensorData;
import com.thoughtworks.iot.repository.SensorDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class KafkaSensorConsumerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SensorDataRepository sensorDataRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaSensorConsumer kafkaSensorConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListenToSensorData_ValidData_HighTemperatureAlert() throws Exception {
        // Arrange
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(101L);
        sensorData.setTemperature(45.5);

        LocalDateTime now = LocalDateTime.now();
        SensorData recentSensorData = new SensorData();
        recentSensorData.setSensorId(101L);
        recentSensorData.setTemperature(42.0);
        recentSensorData.setTimestamp(now.minusMinutes(2));

        when(objectMapper.readValue(anyString(), eq(SensorData.class))).thenReturn(sensorData);
        when(sensorDataRepository.findByTimestampAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(recentSensorData));

        // Act
        kafkaSensorConsumer.listenToSensorData("{\"sensorId\":101,\"temperature\":45.5}");

        // Assert
        ArgumentCaptor<String> alertCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate, times(1)).send(eq("sensor-alerts"), alertCaptor.capture());
        assertEquals("High Temperature Alert for Sensor 101", alertCaptor.getValue());
    }

    @Test
    void testListenToSensorData_ValidData_NoAlert() throws Exception {
        // Arrange
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(102L);
        sensorData.setTemperature(25.0);

        LocalDateTime now = LocalDateTime.now();
        SensorData recentSensorData = new SensorData();
        recentSensorData.setSensorId(102L);
        recentSensorData.setTemperature(28.0);
        recentSensorData.setTimestamp(now.minusMinutes(2));

        when(objectMapper.readValue(anyString(), eq(SensorData.class))).thenReturn(sensorData);
        when(sensorDataRepository.findByTimestampAfter(any(LocalDateTime.class)))
                .thenReturn(List.of(recentSensorData));

        // Act
        kafkaSensorConsumer.listenToSensorData("{\"sensorId\":102,\"temperature\":25.0}");

        // Assert
        verify(kafkaTemplate, never()).send(eq("sensor-alerts"), anyString());
    }

//    @Test
//    void testListenToSensorData_InvalidData() throws Exception {
//        // Arrange
//        when(objectMapper.readValue(anyString(), eq(SensorData.class)))
//                .thenThrow(new com.fasterxml.jackson.core.JsonParseException(null, "Invalid JSON"));
//
//        // Act
//        kafkaSensorConsumer.listenToSensorData("invalid-json");
//
//        // Assert
//        verify(sensorDataRepository, never()).findByTimestampAfter(any(LocalDateTime.class));
//        verify(kafkaTemplate, never()).send(eq("sensor-alerts"), anyString());
//    }

    @Test
    void testListenToSensorData_NoRecentData() throws Exception {
        // Arrange
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(103L);
        sensorData.setTemperature(30.0);

        when(objectMapper.readValue(anyString(), eq(SensorData.class))).thenReturn(sensorData);
        when(sensorDataRepository.findByTimestampAfter(any(LocalDateTime.class)))
                .thenReturn(List.of()); // No recent data

        // Act
        kafkaSensorConsumer.listenToSensorData("{\"sensorId\":103,\"temperature\":30.0}");

        // Assert
        verify(kafkaTemplate, never()).send(eq("sensor-alerts"), anyString());
    }
}
