SparkStreaming实战补充


1. The semantics of streaming systems

2. Kafka Offset Management with Spark Streaming

3. Output operations ensure at-least once semantics (最好是 Exactly-once semantics)




一. 流处理系统的语义

The semantics of streaming systems are often captured in terms of how many times each record can be processed by the system. There are three types of guarantees that a system can provide under all possible operating conditions (despite failures, etc.)

    1. At most once: Each record will be either processed once or not processed at all.
    2. At least once: Each record will be processed one or more times. This is stronger than at-most once as it ensure that no data will be lost. But  
       there may be duplicates.
    3. Exactly once: Each record will be processed exactly once - no data will be lost and no data will be processed multiple times. This is obviously 
       the strongest guarantee of the three.


流处理系统常见的三种语义
1) 最多一次
2) 至少一次
3) 有且仅有一次 *****



二. Kafka整合SparkStreaming的offsets管理宏观介绍

Kafka Offsets常用存储的介质
1) ZK
2) Kafka
3) HBase ==> NoSQL(Redis) ==> RDBMS(MySQL/Oracle/...)
4) HDFS

对于offset管理常见的两步操作: 保存offsets、获取offsets

Spring Data



三. 环境准备
创建一个topic: kafka-topics.sh --create --zookeeper hadoop001:2181 --replication-factor 1 --partitions 1 --topic xtb_offset

producer: kafka-console-producer.sh --broker-list hadoop001:9092 --topic xtb_offset
consumer: kafka-console-consumer.sh --bootstrap-server hadoop001:9092 --topic xtb_offset --from-beginning

编写生产者代码: com.xtb.spark.kafka.KafkaProducerApp




三. offset管理*****
1) offset: checkpoint ==> meta write to hdfs

Note that checkpointing of RDDs incurs the cost of saving to reliable storage. This may cause an increase in the processing time of those batches where RDDs get checkpointed. Hence, the interval of checkpointing needs to be set carefully. At small batch sizes (say 1 second), checkpointing every batch may significantly reduce operation throughput. Conversely, checkpointing too infrequently causes the lineage and task sizes to grow, which may have detrimental effects. For stateful transformations that require RDD checkpointing, the default interval is a multiple of the batch interval that is at least 10 seconds. It can be set by using dstream.checkpoint(checkpointInterval). Typically, a checkpoint interval of 5 - 10 sliding intervals of a DStream is a good setting to try.


2) 详见伪代码: com.xtb.spark.offset.Offset03App



spark-submit \
--master local[2] \
--name Offset02App \
--class com.xtb.spark.offset.Offset02App \
--jars /home/xingtb/lib/spark-streaming-kafka-0-8-assembly_2.11-2.2.3.jar \
/home/xingtb/lib/sparktrain-1.0.jar




四. 计算结果的一致性
Output operations ensure Exactly-once semantics


问题分析: 
step01: 业务逻辑计算, 输出计算结果到外部存储
step02: 保存kafka的offset数据到存储

这两步必须具有事务性

解决方案:
    a. Idempotent 幂等操作: 即重复数据的操作结果一致, MySQL的主键(联合主键), HBase的rowKey和版本号控制
    b. Transactions 事务处理


