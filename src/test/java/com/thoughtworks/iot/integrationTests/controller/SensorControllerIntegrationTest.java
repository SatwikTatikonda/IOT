package com.thoughtworks.iot.integrationTests.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.iot.config.JwtUtil;
import com.thoughtworks.iot.models.SensorType;
import com.thoughtworks.iot.models.Sensors;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.SensorRepository;
import com.thoughtworks.iot.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.crypto.SecretKey;
import javax.management.relation.Role;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
class SensorControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:6.0.6");

    private String secretKey = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    private Sensors sampleSensor;
    private String token;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();
        sensorRepository.deleteAll();
        sampleSensor = new Sensors(1L,"AirSensor", SensorType.LIDAR,"WS3058","META SENSORS");
        User user=new User("kjfsoe","username",List.of("ROLE_ADMIN"),"password");
        userRepository.save(user);
        token=jwtUtil.generateToken(user.getUsername(),user.getRoles());

    }


    @Test
    void testAddSensor() throws Exception {

        mockMvc.perform(post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleSensor))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AirSensor"))
                .andExpect(jsonPath("$.modelNumber").value("WS3058"))
                .andExpect(jsonPath("$.manufacturer").value("META SENSORS"));
    }

    @Test
    void testGetAllSensors() throws Exception {
        mockMvc.perform(post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleSensor))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Fetch all sensors
        mockMvc.perform(get("/api/get")

                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    @Test
    void testDeleteSensor() throws Exception {

        // Add a sample sensor first
        String responseContent = mockMvc.perform(post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleSensor))
                        .header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Sensors createdSensor = objectMapper.readValue(responseContent, Sensors.class);

        // Delete the sensor
        mockMvc.perform(delete("/api/delete/{id}", createdSensor.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Sensor deleted"));
    }

}
