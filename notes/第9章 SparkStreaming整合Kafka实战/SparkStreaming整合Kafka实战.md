SparkStreaming整合Kafka实战

实战一: Receiver-based

实战二: Direct Approach





实战一: Receiver-based
基于Receiver的整合
1) 启动zk: zkServer.sh start
2) 启动kafka: kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties &
3) 创建topic: 
    kafka-topics.sh --create --zookeeper hadoop001:2181 --replication-factor 1 --partitions 1 --topic kafka_streaming_topic
4) 通过控制台测试本topic是否能够正常生产和消费信息
    producer: kafka-console-producer.sh --broker-list hadoop001:9092 --topic kafka_streaming_topic
    consumer: kafka-console-consumer.sh --bootstrap-server hadoop001:9092 --topic kafka_streaming_topic --from-beginning









