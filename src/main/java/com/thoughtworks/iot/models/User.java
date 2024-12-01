package com.thoughtworks.iot.models;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @NotNull(message = "id cant be null")
    private String id;
    @NotNull(message="name cant be null")
    private String username;
    @NotNull(message="role cant be empty")
    private List<String> roles;
    @NotNull(message="password cant be empty")
    private String password;
}
