//package com.bank.history;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataSender {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    public DataSender(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendDataToKafka(String data) {
//        kafkaTemplate.send("my-topic", data);
//    }
//}