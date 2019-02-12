分布式日志收集框架Flume

1. 业务现状分析                 4. Flume环境部署
2. Flume概述                    5. Flume实战 ***重要
3. Flume架构及核心组件



1. 业务现状分析 
	详见: Flume业务背景需求图.png
	如何解决我们的数据从其他的server上移动到Hadoop之上 ???
	1) shell cp Hadoop集群的机器上, hadoop fs -put .../     ---> 磁盘读写，网络开销，无法监控，服务器宕机，时效性差 等问题...

	===> Flume的诞生
	1. WebServer/ApplicationServer分散在各个机器上
	2. 想在大数据平台Hadoop上进行统计分析
	3. 日志如何收集到Hadoop平台上
	4. 解决方案及存在的问题



2. Flume概述 
	官网很详细, 一定要去看官网***
	Flume is a distributed, reliable, and available service for efficiently collecting(收集), aggregating(聚合), and moving(移动) large amounts of log data.

	设计目标: 
		可靠性
		扩展性
		管理性

	业界同类产品的对比: 
		(***)Flume: Cloudera/Apache   Java
		Scribe: Facebook   C/C++   不再维护
		Chunkwa: Yahoo/Apache   Java   不再维护
		Fluentd: Ruby
		(***)Logstash: ELK(ElasticSearch, Kibana)



Flume架构及核心组件
1) Source   收集

2) Channel  聚集

3) Sink     输出


4. Flume环境部署
	Flume环境搭建的前置要求:
	1) Java Runtime Environment - Java 1.8 or later
	2) Memory - Sufficient memory for configurations used by sources, channels or sinks
	3) Disk Space - Sufficient disk space for configurations used by channels or sinks
	4) Directory Permissions - Read/Write permissions for directories used by agent


	安装JDK1.8

	flume下载: wget http://archive.cloudera.com/cdh5/cdh/5/flume-ng-1.6.0-cdh5.7.0.tar.gz
	解压到app目录下
	配置系统环境变量:
	~/.bash_profile 添加: 
	  export FLUME_HOME=/home/xingtb/app/apache-flume-1.6.0-cdh5.7.0-bin
	  export PATH=$FLUME_HOME/bin:$PATH
	source一下使其生效

	flume-env.sh的配置: export JAVA_HOME=/home/xingtb/app/jdk1.8.0_181




5. Flume实战
	

需求一: 从指定的网络端口采集数据输出到控制台

# example.conf: A single-node Flume configuration
使用Flume的关键就是写配置文件
1) 配置Source
2) 配置Channel
3) 配置Sink
4) 把以上三个组件串起来

a1: agent名称
r1: source的名称
k1: sink的名称
c1: channel的名称

# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = netcat
a1.sources.r1.bind = hadoop001
a1.sources.r1.port = 44444

# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1


启动agent
flume-ng agent -n $agent_name -c conf -f conf/flume-conf.properties.template
flume-ng agent \
--name a1 \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/example.conf \
-Dflume.root.logger=INFO,console

使用telnet进行测试: telnet localhost 44444

Event: { headers:{} body: 68 65 6C 6C 6F 0D hello. }
Event 是Flume数据传输的基本单元
Event = 可选的header + byte array



需求二: 监控一个文件实时采集新增的数据输出到控制台
Agent选型: exec source + memory channel + logger sink

# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
a1.sources.r1.type = exec
a1.sources.r1.command = tail -F /home/xingtb/data/data.log
a1.sources.r1.shell = /bin/sh -c

# Describe the sink
a1.sinks.k1.type = logger

# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100

# Bind the source and sink to the channel
a1.sources.r1.channels = c1
a1.sinks.k1.channel = c1

启动agent
flume-ng agent \
--name a1 \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/exec-memory-logger.conf \
-Dflume.root.logger=INFO,console


需求三(*****): 将服务器A的日志实时采集到服务器B上
	详见: 需求三的设计图
	技术选型: 
		exec source + memory channel + avro sink
		avro source + memory channel + logger sink

exec-memory-avro.conf

exec-memory-avro.sources = exec-source
exec-memory-avro.sinks = avro-sink
exec-memory-avro.channels = memory-channel

exec-memory-avro.sources.exec-source.type = exec
exec-memory-avro.sources.exec-source.command = tail -F /home/xingtb/data/data.log
exec-memory-avro.sources.exec-source.shell = /bin/sh -c

exec-memory-avro.sinks.avro-sink.type = avro
exec-memory-avro.sinks.avro-sink.hostname = hadoop001
exec-memory-avro.sinks.avro-sink.port = 44444

exec-memory-avro.channels.memory-channel.type = memory
exec-memory-avro.channels.memory-channel.capacity = 1000
exec-memory-avro.channels.memory-channel.transactionCapacity = 100

exec-memory-avro.sources.exec-source.channels = memory-channel
exec-memory-avro.sinks.avro-sink.channel = memory-channel

----------------------------------------------------------------------
avro-memory-logger.conf

avro-memory-logger.sources = avro-source
avro-memory-logger.sinks = logger-sink
avro-memory-logger.channels = memory-channel

avro-memory-logger.sources.avro-source.type = avro
avro-memory-logger.sources.avro-source.bind = hadoop001
avro-memory-logger.sources.avro-source.port = 44444

avro-memory-logger.sinks.logger-sink.type = logger

avro-memory-logger.channels.memory-channel.type = memory
avro-memory-logger.channels.memory-channel.capacity = 1000
avro-memory-logger.channels.memory-channel.transactionCapacity = 100

avro-memory-logger.sources.avro-source.channels = memory-channel
avro-memory-logger.sinks.logger-sink.channel = memory-channel


注意启动顺序: 先启动监听端口的agent
flume-ng agent \
--name avro-memory-logger \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/demand03/avro-memory-logger.conf \
-Dflume.root.logger=INFO,console


flume-ng agent \
--name exec-memory-avro \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/demand03/exec-memory-avro.conf \
-Dflume.root.logger=INFO,console


