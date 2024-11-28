package com.thoughtworks.iot.dtos;

import lombok.Data;

@Data
public class AuthRequest {

    private Long id;
    private String username;
    private String password;
}
