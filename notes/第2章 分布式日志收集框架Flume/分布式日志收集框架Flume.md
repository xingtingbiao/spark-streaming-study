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
	需求: 从指定的网络端口采集数据输出到控制台





