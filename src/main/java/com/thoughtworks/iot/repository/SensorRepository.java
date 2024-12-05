package com.thoughtworks.iot.repository;

import com.thoughtworks.iot.models.Sensors;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends MongoRepository<Sensors, Long> {

    public List<Sensors> findAll();

    public void deleteById(Long id);

    public Sensors save(Sensors sensors);


}
