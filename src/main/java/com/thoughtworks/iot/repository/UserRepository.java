package com.thoughtworks.iot.repository;

import com.thoughtworks.iot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    public Optional<User> findByUsername(String username);
}
