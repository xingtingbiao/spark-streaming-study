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


