Spark Streaming整合Flume&Kafka打造通用流处理平台

Tasks: 
1. 整合日志输出到Flume
2. 整合Flume到Kafka
3. 整合Kafka到Spark Streaming 
4. Spark Streaming对接收到的数据进行处理




一. 整合日志输出到Flume
1) 使用log4j做一个简单的日志生成器: LoggerGenerator
2) 使用Flume采集Log4j产生的日志
    
a. 编写agent配置文件: 
streaming.conf

agent1.sources=avro-source
agent1.channels=logger-channel
agent1.sinks=log-sink

#define source
agent1.sources.avro-source.type=avro
agent1.sources.avro-source.bind=0.0.0.0
agent1.sources.avro-source.port=41414

#define channel
agent1.channels.logger-channel.type=memory

#define sink
agent1.sinks.log-sink.type=logger

agent1.sources.avro-source.channels=logger-channel
agent1.sinks.log-sink.channel=logger-channel


b. 测试启动:
flume-ng agent \
--name agent1 \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/log4j_conf/streaming.conf \
-Dflume.root.logger=INFO,console


c. 编写本地log4j往flume监听的端口41414发送日志. 具体参考Flume官网的: Log4J Appender

编译出错:
java.lang.ClassNotFoundException: org.apache.flume.clients.log4jappender.Log4jAppender
缺少jar包




二. 整合Flume到Kafka
a. 创建一个topic: streamingtopic
kafka-topics.sh --create --zookeeper hadoop001:2181 --replication-factor 1 --partitions 1 --topic streamingtopic

b. 编写agent配置文件: 
streaming2.conf

agent1.sources=avro-source
agent1.channels=logger-channel
agent1.sinks=kafka-sink

#define source
agent1.sources.avro-source.type=avro
agent1.sources.avro-source.bind=0.0.0.0
agent1.sources.avro-source.port=41414

#define channel
agent1.channels.logger-channel.type=memory
agent1.channels.logger-channel.capacity = 1000
agent1.channels.logger-channel.transactionCapacity = 100

#define sink
agent1.sinks.kafka-sink.type=org.apache.flume.sink.kafka.KafkaSink
agent1.sinks.kafka-sink.brokerList=hadoop001:9092
agent1.sinks.kafka-sink.topic=streamingtopic
agent1.sinks.kafka-sink.batchSize=10
agent1.sinks.kafka-sink.requiredAcks=1

agent1.sources.avro-source.channels=logger-channel
agent1.sinks.kafka-sink.channel=logger-channel


c. 测试启动:
flume-ng agent \
--name agent1 \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/kafka_conf/streaming2.conf \
-Dflume.root.logger=INFO,console




三. 整合Kafka到Spark Streaming
a. 详见代码: com.xtb.spark.streaming.KafkaStreamingApp




四. Spark Streaming对接收到的数据进行处理
TODO...



五. 本地测试和生成环境的拓展
我们现在是在本地进行测试的, 在IDEA中运行LoggerGenerator, 然后使用Flume、Kafka已经Spark Streaming进行处理

在生产环境上肯定不是这么干的, 怎么干呢? 
1) 打包jar, 执行LoggerGenerator类
2) Flume、Kafka和我们现在测试是一样的
3) Spark Streaming也是需要打成jar包, 然后使用spark-submit的方式进行提交到环境上执行, 可以根据自己的实际情况选择具体的运行模式: local/yarn/standalone/mesos

在生产上, 整个流处理的流程和现在写的简单的测试demo都是一样的, 区别只是在拿到message数据之后如何做具体的业务逻辑(根据具体的业务场景)




