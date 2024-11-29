//package com.thoughtworks.iot.controller;
//
//import com.thoughtworks.iot.models.SensorType;
//import com.thoughtworks.iot.models.Sensors;
//import com.thoughtworks.iot.service.SensorService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
//                .andExpect(jsonPath("$.size()").value(2));
//    }
//}
