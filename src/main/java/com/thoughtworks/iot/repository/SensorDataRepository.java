package com.thoughtworks.iot.repository;

import com.thoughtworks.iot.dtos.Temperature_TimeStamp_Binder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SensorDataRepository {
    public Map<Long, List<Temperature_TimeStamp_Binder>> saveSensorData(Long id, Map<Long, List<Temperature_TimeStamp_Binder>> temperatureMap, Temperature_TimeStamp_Binder binder) {

        temperatureMap.putIfAbsent(id, new ArrayList<>());
        temperatureMap.get(id).add(binder);
        System.out.println(temperatureMap);
        return temperatureMap;

    }
}
