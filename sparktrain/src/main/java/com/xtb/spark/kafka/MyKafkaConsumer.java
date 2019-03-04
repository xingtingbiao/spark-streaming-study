/*
package com.xtb.spark.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

*/
/**
 * Kafka 消费者的练习
 *//*

public class MyKafkaConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaProperties.BROKER_LIST);
        props.put("group.id", KafkaProperties.GROUP_ID);
        props.put("enable.auto.commit", "false ");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Collections.singletonList(KafkaProperties.TOPIC));
        //用于分配topic和partition
        //不改变当前offset，指定从这个topic和partition的开始位置获取。
        consumer.assign(Collections.singletonList(new TopicPartition(KafkaProperties.TOPIC, 0)));
        // 这里使用kafka2.1.0, 为了使用对接SparkStreaming, 版本改为0.9.0.0, 所以将以下代码注释
//        consumer.seekToBeginning(Collections.singletonList(new TopicPartition(KafkaProperties.TOPIC, 0)));
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(100));
//            for (ConsumerRecord<String, String> record : records)
//                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
//        }
    }
}
*/
