package com.thoughtworks.iot.changeLog;
//
//import com.github.cloudyrock.mongock.ChangeLog;
//import com.github.cloudyrock.mongock.ChangeSet;
import com.thoughtworks.iot.models.User;
import com.thoughtworks.iot.repository.UserRepository;
import io.mongock.api.annotations.ChangeUnit;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@ChangeUnit(order = "001", id = "addDefaultUsers", author = "developer")
public class DatabaseChangelog {


//    @ChangeSet(order = "001", id = "addDefaultUsers", author = "developer")
    public void addDefaultUsers(UserRepository userRepository) {

        System.out.println("saving users using mongock");
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
        admin.setRoles(List.of("ROLE_ADMIN"));

        User user = new User();
        user.setUsername("user");
        user.setPassword(new BCryptPasswordEncoder().encode("user123"));
        user.setRoles(List.of("ROLE_USER"));

        userRepository.save(admin);
        userRepository.save(user);
    }

}
