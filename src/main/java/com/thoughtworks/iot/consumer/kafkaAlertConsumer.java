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
        getAllTopics("localhost:9092");
    }

    public void getAllTopics(String bootstrapServers){

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient adminClient = AdminClient.create(props)) {

            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(true); // Include internal topics if needed
            System.out.println("getting all topics "+adminClient.listTopics(options).names().get());

        } catch (InterruptedException | ExecutionException e) {

            System.out.println("got some error whiel retreiving  topics "+e.getMessage());
        }

    }

}
