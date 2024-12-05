package com.thoughtworks.iot.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Temperature_TimeStamp_Binder {

    Double temperature;
    LocalDateTime timestamp;

}
