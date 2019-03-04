SparkStreaming整合Kafka实战

实战一: Receiver-based

实战二: Direct Approach





实战一: Receiver-based
基于Receiver的整合
原理: 启动一个接收器去接收kafka的数据, 但是一旦运行失败就会出现数据丢失的情况, 不得不启用日志存储功能(先写到日志备份)
1) 启动zk: zkServer.sh start
2) 启动kafka: kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties &
3) 创建topic: 
    kafka-topics.sh --create --zookeeper hadoop001:2181 --replication-factor 1 --partitions 1 --topic kafka_streaming_topic
4) 通过控制台测试本topic是否能够正常生产和消费信息
    producer: kafka-console-producer.sh --broker-list hadoop001:9092 --topic kafka_streaming_topic
    consumer: kafka-console-consumer.sh --bootstrap-server hadoop001:9092 --topic kafka_streaming_topic --from-beginning

5) IDEA上编写代码: com.xtb.spark.streaming.KafkaReceiverWordCount

6) 服务器上运行:
spark-submit \
--class com.xtb.spark.streaming.KafkaReceiverWordCount \
--master local[2] \
--name KafkaReceiverWordCount \
--jars /home/xingtb/lib/spark-streaming-kafka-0-8-assembly_2.11-2.2.3.jar \
/home/xingtb/lib/sparktrain-1.0.jar hadoop001:2181 test kafka_streaming_topic 1






实战一: Direct Approach
基于Direct的整合
原理: 周期性的检查kafka的offsets(偏移量), 拿到偏移量后直接调用kafka的API读取分区数据
1) 详见开发代码: com.xtb.spark.streaming.KafkaDirectWordCount
2) 







