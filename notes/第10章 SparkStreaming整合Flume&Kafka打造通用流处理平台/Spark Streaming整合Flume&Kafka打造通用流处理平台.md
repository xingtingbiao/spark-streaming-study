Spark Streaming整合Flume&Kafka打造通用流处理平台

Tasks: 
1. 整合日志输出到Flume
2. 整合Flume到Kafka
3. 整合Kafka到Spark Streaming 
4. Spark Streaming对接收到的数据进行处理




一. 整合日志输出到Flume
1) 使用log4j做一个简单的日志生成器: LoggerGenerator
2) 使用Flume采集Log4j产生的日志
    a. 编写agent配置文件: streaming.conf

agent1.sources=avro-source
agent1.channels=logger-channel
agent1.sinks=log-sink

#define source






