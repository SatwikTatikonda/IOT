package com.thoughtworks.iot.migrations;


import com.thoughtworks.iot.models.User;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.jayway.jsonpath.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@ChangeUnit(id="MigrationChangeUnitId", order = "001", author = "mongock_test", systemVersion = "1")
public class DatabaseChangeLog {

    private final MongoTemplate mongoTemplate;
    private PasswordEncoder passwordEncoder;

    public DatabaseChangeLog(MongoTemplate mongoTemplate,PasswordEncoder passwordEncoder) {
        System.out.println("in DatabaseChangeLog");
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Execution
    public void migrationMethod() {

        System.out.println("migrationMethod");
        if (mongoTemplate.findAll(User.class).isEmpty()) {

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("password1")); // Pre-encoded password
            adminUser.setRoles(List.of("ROLE_ADMIN"));

            User regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword(passwordEncoder.encode("password2")); // Pre-encoded password
            regularUser.setRoles(List.of("ROLE_USER"));

            mongoTemplate.insertAll(List.of(adminUser, regularUser));
        }


    }
    @RollbackExecution
    public void rollbackDefaultUsers(MongoTemplate mongoTemplate) {
        // Remove the users created by this migration
        mongoTemplate.remove(
                query((CriteriaDefinition) where("username").in("admin", "user")),
                User.class
        );
    }
}



