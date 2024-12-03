package com.thoughtworks.iot.mogrations;

import com.thoughtworks.iot.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest
class DatabaseChangeLogTest {
    private MongoTemplate mongoTemplate;
    private PasswordEncoder passwordEncoder;
    private DatabaseChangeLog databaseChangeLog;

    @BeforeEach
    void setUp() {
        mongoTemplate = mock(MongoTemplate.class);
        passwordEncoder = mock(PasswordEncoder.class);
        databaseChangeLog = new DatabaseChangeLog(mongoTemplate, passwordEncoder);
    }

    @Test
    void testMigrationMethod() {
        // Arrange
        when(mongoTemplate.findAll(User.class)).thenReturn(List.of());
        when(passwordEncoder.encode("password1")).thenReturn("encodedPassword1");
        when(passwordEncoder.encode("password2")).thenReturn("encodedPassword2");

        // Act
        databaseChangeLog.migrationMethod();

        // Assert
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(mongoTemplate, times(1)).insertAll(captor.capture());

        List<User> capturedUsers = captor.getValue();
        assertEquals(2, capturedUsers.size());

        User adminUser = capturedUsers.get(0);
        assertEquals("admin", adminUser.getUsername());
        assertEquals("encodedPassword1", adminUser.getPassword());
        assertEquals(List.of("ROLE_ADMIN"), adminUser.getRoles());

        User regularUser = capturedUsers.get(1);
        assertEquals("user", regularUser.getUsername());
        assertEquals("encodedPassword2", regularUser.getPassword());
        assertEquals(List.of("ROLE_USER"), regularUser.getRoles());
    }



}