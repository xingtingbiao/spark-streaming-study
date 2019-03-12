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


3) 打通Flume&Kafka&Spark Streaming线路
    a. 详见代码: com.xtb.spark.project.StateCountStreamingApp



4) 数据清洗(按照需求对实时产生的点击流数据进行数据清洗)
    a. 从原始日志中取出我们所需的字段信息即可
    清洗的数据如下:

ClickLog(72.55.187.46,20190311235501,131,500,http://www.sogou.com/web?query=Spark SQL实战)
ClickLog(187.167.124.63,20190311235501,145,200,-)
ClickLog(10.29.156.167,20190311235501,112,500,-)
ClickLog(87.72.132.124,20190311235501,128,404,-)
ClickLog(187.167.55.143,20190311235501,112,500,-)
ClickLog(167.46.29.63,20190311235501,112,500,-)
ClickLog(10.167.46.124,20190311235501,112,404,-)
ClickLog(98.72.132.29,20190311235501,145,404,http://search.yahoo.com/search?p=Hadoop基础)
ClickLog(187.143.55.168,20190311235501,131,200,-)
ClickLog(168.10.87.29,20190311235501,145,500,http://cn.bing.com/search?q=大数据面试)
    
补充注意: 配置稍微高一点
    hadoop001: 8Core  8G



5) 需求功能开发
    (a***). 统计今天到现在为止实战课程的访问量

    yyyyMMdd     courseId

使用数据库来进行存储我们的统计结果
    Spark Streaming 把统计结果写到数据库里
    可视化前端根据: yyyyMMdd    courseId 把数据库里面的统计结果展示出来


选择什么数据库作为统计结果的存储呢?
    RDBMS: Mysql、Oracle...
        day       course_id     click_count
        20181111     101             10
        20181111     102             10

        下一个批次数据进来以后: 
            20181111 + 101 ==> click_count(select) + 下一个批次的统计结果 ==> 写入到数据库中 



    NoSQL: HBase、Redis...
        本次实战采用HBase: 一个API就能搞定, 非常方便
            20181111 + 101 ==> click_count + 下一个批次的统计结果
        本次实战为什么选用HBase的一个重要的原因

    前提: 
        HDFS
        Zookeeper
        HBase

    HBase表设计: 
        创建表: 
            create 'imooc_course_clickcount', 'info'
        RowKey设计(核心): 
            day_courseid


如何使用Scala来操作HBase?
详见代码: com.xtb.spark.dao.CourseClickCountDao


    
    (b***). 统计今天到现在为止从搜索引擎引流过来的实战课程的访问量





五. 生产环境运行



