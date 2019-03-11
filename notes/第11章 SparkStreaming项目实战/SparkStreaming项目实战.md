SparkStreaming 项目实战

目录:
    1. 需求说明
    2. 互联网访问日志概述
    3. 功能开发及本地运行
    4. 生产环境运行




一. 需求说明
   a. 今天到现在为止实战课程的访问量

   b. 今天到现在为止从搜索引擎引流过来的实战课程的访问量




二. 互联网访问日志概述
1) 为什么要记录用户访问行为日志
    1. 网站页面的访问量
    2. 网站的粘性
    3. 推荐

2) 用户行为日志的内容
    1. 详见用户行为日志内容图.png

3) 用户行为日志分析的意义
    1. 网站的眼睛
    2. 网站的神经
    3. 网站的大脑




三. 功能开发及本地运行
1) 使用Python脚本实时产生数据
    1. Python实时日志产生器开发

2) 通过定时调度工具每一分钟产生一批数据
    Linux crontab
    网站: http://tool.lu/crontab
    每一分钟执行一次的crontab表达式: */1 * * * * 

    如何开启: 
    crontab -e
        */1 * * * *  /home/xingtb/data/project/generate_log.sh

crontab补充:
Linux
*    *    *    *    *    *
-    -    -    -    -    -
|    |    |    |    |    |
|    |    |    |    |    + year [optional]
|    |    |    |    +----- day of week (0 - 7) (Sunday=0 or 7)
|    |    |    +---------- month (1 - 12)
|    |    +--------------- day of month (1 - 31)
|    +-------------------- hour (0 - 23)
+------------------------- min (0 - 59)

Java(Spring)
*    *    *    *    *    *    *
-    -    -    -    -    -    -
|    |    |    |    |    |    |
|    |    |    |    |    |    + year [optional]
|    |    |    |    |    +----- day of week (0 - 7) (Sunday=0 or 7)
|    |    |    |    +---------- month (1 - 12)
|    |    |    +--------------- day of month (1 - 31)
|    |    +-------------------- hour (0 - 23)
|    +------------------------- min (0 - 59)
+------------------------------ second (0 - 59)




四. 对接日志==>Flume==>Kafka==>Spark Streaming
1) 对接python日志产生器输出的日志到Flume
streaming-project.conf

选型: access.log  ==>  控制台输出
      exec
      memory
      logger


exec-memory-logger.sources = exec-source
exec-memory-logger.sinks = logger-sink
exec-memory-logger.channels = memory-channel

exec-memory-logger.sources.exec-source.type = exec
exec-memory-logger.sources.exec-source.command = tail -F /home/xingtb/data/project/logs/access.log
exec-memory-logger.sources.exec-source.shell = /bin/sh -c

exec-memory-logger.channels.memory-channel.type = memory
exec-memory-logger.channels.memory-channel.capacity = 1000
exec-memory-logger.channels.memory-channel.transactionCapacity = 100

exec-memory-logger.sinks.logger-sink.type = logger

exec-memory-logger.sources.exec-source.channels = memory-channel
exec-memory-logger.sinks.logger-sink.channel = memory-channel


启动agent
flume-ng agent \
--name exec-memory-logger \
--conf $FLUME_HOME/conf \
--conf-file /home/xingtb/data/project/streaming-project.conf \
-Dflume.root.logger=INFO,console



2) 日志 ==> Flume ==> Kafka
    a. 启动zk: ./zkServer.sh start
    b. 启动Kafka Server: ./kafka-server-start.sh -daemon $KAFKA_HOME/config/server.properties &
    c. 修改Flume配置文件使得flume sink数据到Kafka


streaming-project2.conf

exec-memory-kafka.sources = exec-source
exec-memory-kafka.sinks = kafka-sink
exec-memory-kafka.channels = memory-channel

exec-memory-kafka.sources.exec-source.type = exec
exec-memory-kafka.sources.exec-source.command = tail -F /home/xingtb/data/project/logs/access.log
exec-memory-kafka.sources.exec-source.shell = /bin/sh -c

exec-memory-kafka.channels.memory-channel.type = memory
exec-memory-kafka.channels.memory-channel.capacity = 1000
exec-memory-kafka.channels.memory-channel.transactionCapacity = 100

exec-memory-kafka.sinks.kafka-sink.type = org.apache.flume.sink.kafka.KafkaSink
exec-memory-kafka.sinks.kafka-sink.brokerList=hadoop001:9092
exec-memory-kafka.sinks.kafka-sink.topic=streamingtopic
exec-memory-kafka.sinks.kafka-sink.batchSize=10
exec-memory-kafka.sinks.kafka-sink.requiredAcks=1

exec-memory-kafka.sources.exec-source.channels = memory-channel
exec-memory-kafka.sinks.kafka-sink.channel = memory-channel


启动agent
flume-ng agent \
--name exec-memory-kafka \
--conf $FLUME_HOME/conf \
--conf-file /home/xingtb/data/project/streaming-project2.conf \
-Dflume.root.logger=INFO,console


kafka-console-consumer.sh --zookeeper hadoop001:2181 --topic streamingtopic







五. 生产环境运行



