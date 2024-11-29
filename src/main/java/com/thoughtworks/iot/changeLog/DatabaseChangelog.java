package com.thoughtworks.iot.changeLog;
//
//import com.github.cloudyrock.mongock.ChangeLog;
//import com.github.cloudyrock.mongock.ChangeSet;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.UserRepository;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@ChangeUnit(order = "001", id = "addDefaultUsers", author = "developer")
public class DatabaseChangelog {


    MongoTemplate mongoTemplate;
    PasswordEncoder passwordEncoder;

    public DatabaseChangelog(MongoTemplate mongoTemplate,PasswordEncoder passwordEncoder) {
        System.out.println("mongock constructor");
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Execution
    public void addDefaultUsers(MongoTemplate mongoTemplate) {

        System.out.println("saving users using mongock");
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(List.of("ROLE_ADMIN"));

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(List.of("ROLE_USER"));

        mongoTemplate.save(admin);
        mongoTemplate.save(user);
    }

}
