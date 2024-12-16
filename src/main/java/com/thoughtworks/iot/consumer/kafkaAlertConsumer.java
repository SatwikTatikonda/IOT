package com.thoughtworks.iot.consumer;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class kafkaAlertConsumer {


    @KafkaListener(topics="sensor-alerts",groupId="group2")
    public void consumeAlerts(String message){
        System.out.println(message);

    }


}
