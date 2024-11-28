package com.thoughtworks.iot.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    @NotNull(message = "id cant be null")
    private long id;
    @NotNull(message="name cant be null")
    private String username;
    @NotNull(message="role cant be empty")
    private List<String> roles;
    @NotNull(message="password cant be empty")
    private String password;
}
