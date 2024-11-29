//package com.thoughtworks.iot.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.thoughtworks.iot.config.JwtAuthenticationFilter;
//import com.thoughtworks.iot.controllers.SensorController;
//import com.thoughtworks.iot.models.SensorType;
//import com.thoughtworks.iot.models.Sensors;
//import com.thoughtworks.iot.service.SensorService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(value=SensorController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
//@ExtendWith(SpringExtension.class)
//@MockBean(JwtAuthenticationFilter.class) // Mock the JwtAuthenticationFilter
//
//public class SensorControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SensorService sensorService;
//
//    @Test
//    void testGetAllSensors() throws Exception {
//        List<Sensors> sensors = Arrays.asList(new Sensors(1L, "AirSensor", SensorType.PROXIMITY,"WD1213","TATA"), new Sensors(21L, "TouchSensor", SensorType.LIDAR,"TT1343","TESLA"));
//        Mockito.when(sensorService.getSensors()).thenReturn(sensors);
//
//        mockMvc.perform(get("/api/public"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//
//    @Test
//    void testAddSensor() throws Exception {
//        Sensors sensor = new Sensors(1L, "AirSensor", SensorType.PROXIMITY,"WD1213","TATA");
//        Mockito.when(sensorService.create(any(Sensors.class))).thenReturn(sensor);
//
//        mockMvc.perform(post("/api/public")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(sensor)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("AirSensor"))
//                .andExpect(jsonPath("$.type").value("PROXIMITY"))
//                .andExpect(jsonPath("$.modelNumber").value("WD1213"))
//                .andExpect(jsonPath("$.manufacturer").value("TATA"))
//        ;
//    }
//
//
//    @Test
//    void testDeleteSensor() throws Exception {
//        Mockito.when(sensorService.deleteSensor(eq(1L))).thenReturn("Sensor deleted");
//
//        mockMvc.perform(delete("/api/admin/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Sensor deleted"));
//    }
//
//
//}
