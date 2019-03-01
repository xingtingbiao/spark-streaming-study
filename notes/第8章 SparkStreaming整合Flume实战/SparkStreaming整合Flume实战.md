Spark Streaming整合Flume实战

实战一: Flume-style Push-based Approach

实战二: Pull-based Approach using a Custom Sink




实战一: Flume-style Push-based Approach
1) Push方式整合

step1: Flume配置文件的编写

Flume Agent的编写: flume_push_streaming.conf

simple-agent.sources = netcat-source
simple-agent.sinks = avro-sink
simple-agent.channels = memory-channel

simple-agent.sources.netcat-source.type = netcat
simple-agent.sources.netcat-source.bind = hadoop001
simple-agent.sources.netcat-source.port = 44444

simple-agent.sinks.avro-sink.type = avro
simple-agent.sinks.avro-sink.hostname = hadoop001
simple-agent.sinks.avro-sink.port = 41414

simple-agent.channels.memory-channel.type = memory
simple-agent.channels.memory-channel.capacity = 1000
simple-agent.channels.memory-channel.transactionCapacity = 100

simple-agent.sources.netcat-source.channels = memory-channel
simple-agent.sinks.avro-sink.channel = memory-channel

flume-ng agent \
--name simple-agent \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/spark_conf/flume_push_streaming.conf \
-Dflume.root.logger=INFO,console

注意: 本地测试需要将avro-sink.hostname写成本地代码所在的主机IP(192.168.2.191)


step2: 代码的开发以及本地的联调
1) 启动SparkStreaming作业
2) 启动Flume agent
3) 通过 telnet localhost 44444 输入数据, 观察IDEA控制台的输出结果


step3: 服务器环境联调
spark-submit \
--class com.xtb.spark.streaming.FlumePushWordCount \
--master local[2] \
--packages org.apache.spark:spark-streaming-flume_2.11:2.2.3 \
/home/xingtb/lib/sparktrain-1.0.jar \
hadoop001 41414


spark-submit \
--class com.xtb.spark.streaming.FlumePushWordCount \
--master local[2] \
--jars /home/xingtb/lib/spark-streaming-flume-assembly_2.11-2.2.3.jar \
/home/xingtb/lib/sparktrain-1.0.jar \
hadoop001 41414





实战二: Pull-based Approach using a Custom Sink
1) Pull方式整合



