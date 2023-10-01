//package com.bank.profile;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaConsumer {
//
//    @KafkaListener(topics = "my-topic", groupId = "my-group")
//    public void receiveMessage(String message) {
//        // Обработка полученного сообщения
//        System.out.println("Received message: " + message);
//    }
//}