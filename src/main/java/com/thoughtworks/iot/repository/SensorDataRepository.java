package com.thoughtworks.iot.repository;

import com.thoughtworks.iot.models.SensorData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends MongoRepository<SensorData,String> {

     SensorData save(SensorData sensorData);

    List<SensorData> findByTimestampAfter(LocalDateTime fiveMinutesAgo);
}
