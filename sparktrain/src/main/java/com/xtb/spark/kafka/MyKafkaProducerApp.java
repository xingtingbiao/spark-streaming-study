package com.xtb.spark.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MyKafkaProducerApp {
    public static void main(String[] args) throws InterruptedException {
        MyKafkaProducer myKafkaProducer = new MyKafkaProducer(KafkaProperties.TOPIC);
        Producer<String, String> producer = myKafkaProducer.getProducer();
        for (int i = 0; i < 20; i++) {
            producer.send(new ProducerRecord<>(KafkaProperties.TOPIC, "message-" + String.valueOf(i)));
            Thread.sleep(2000);
        }
        System.out.println("send over!");
        producer.close();
    }
}
