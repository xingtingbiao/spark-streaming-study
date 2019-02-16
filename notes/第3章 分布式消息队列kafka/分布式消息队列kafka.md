分布式消息队列kafka

1. Kafka概述					4. Kafka容错性测试
2. Kafka架构及核心概念			5. Kafka API编程
3. Kafka部署及使用              6. Kafka实战




1. Kafka概述
	官网: kafka.apache.org
	和消息系统类似

	消息中间件: 生产者和消费者
	举个例子: 

	妈妈: 生产者
	你: 消费者
	馒头: 数据量、消息

		正常情况下: 生产一个，消费一个
		其他情况: 
			一直生产，你吃到某一个馒头时, 你卡住(机器故障), 馒头就丢失了
			一直生产， 做馒头的速度快，你来不及吃，馒头也会丢失

		解决方案: 拿个篮子，做好的馒头先放到篮子里，你要吃的时候去篮子中去取即可

	篮子/框: Kafka
		当篮子满了，馒头就装不下了, 咋办
		多准备几个篮子 === Kafka的扩容





2. Kafka架构及核心概念
	producer: 生产者, 就是生产馒头(老妈)
	consumer: 消费者, 就是吃馒头的(你)
	broker: 篮子
	topic: 主题, 给馒头带一个标签, topic-a的馒头是给你吃的, topic-b的馒头是给你弟弟吃的



3. Kafka部署及使用
	1) 单节点单Broker部署及使用
	2) 单节点多Broker部署及使用
	3) 多节点多Broker部署及使用


$KAFKA_HOME/config/server.properties
# The id of the broker. This must be set to a unique integer for each broker.
broker.id=0

# The address the socket server listens on. It will get the value returned from 
# java.net.InetAddress.getCanonicalHostName() if not configured.
#   FORMAT:
#     listeners = listener_name://host_name:port
#   EXAMPLE:
#     listeners = PLAINTEXT://your.host.name:9092
listeners=PLAINTEXT://:9092

############################# Log Basics #############################

# A comma separated list of directories under which to store log files
log.dirs=/tmp/kafka-logs

zookeeper.connect=hadoop001:2181

1. 单节点单broker的使用: 
启动kafka: bin/kafka-server-start.sh config/server.properties

Let's create a topic named "test" with a single partition and only one replica:
创建topic: 需要指定zookeeper
1> bin/kafka-topics.sh --create --zookeeper hadoop001:2181 --replication-factor 1 --partitions 1 --topic hello_topic
kafka-topics.sh --list --zookeeper hadoop001:2181

启动生产者: 需要指定具体的某一个broker(很容易理解即选择具体哪一个篮子装东西)
kafka-console-producer.sh --broker-list hadoop001:9092 --topic hello_topic

启动消费者: 需要指定具体的某一个broker(很容易理解即选择具体哪一个篮子装东西)
kafka-console-consumer.sh --bootstrap-server hadoop001:9092 --topic hello_topic --from-beginning

--from-beginning: 顾名思义就是从头开始消费

查看所有topic的信息: kafka-topics.sh --describe --zookeeper hadoop001:2181
查看具体topic的信息: kafka-topics.sh --describe --zookeeper hadoop001:2181 --topic hello_topic


2. 单节点多Broker部署及使用
server-1.properties
server-2.properties
server-3.properties


> bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server-1.properties &
...
> bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/server-2.properties &
...

创建一个多副本一个分区的topic
> bin/kafka-topics.sh --create --zookeeper hadoop001:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic

生产者: 
kafka-console-producer.sh --broker-list hadoop001:9093,hadoop001:9094,hadoop001:9095 --topic my-replicated-topic

消费者: 
kafka-console-consumer.sh --bootstrap-server hadoop001:9093,hadoop001:9094,hadoop001:9095 --from-beginning --topic my-replicated-topic

注意: *****
__consumer_offsets的问题, 这是kafka自动创建的，默认50个，但是都存在一台kafka服务器上，这是不是就存在很明显的单点故障
经测试，如果将存储consumer_offsets的这台机器kill掉，所有的消费者都停止消费了。请问这个问题是怎么解决的呢？

由于__consumer_offsets这个用于存储offset的分区是由kafka服务器默认自动创建的，那么它在创建该分区的时候，分区数和副本数的依据是什么？
分区数是固定的50，这个没什么可怀疑的，副本数呢？应该是一个默认值1，依据是，如果我们没有在server.properties文件中指定topic分区的副本数的话，它的默认值就是1。
__consumer_offsets是一个非常重要的topic，我们怎么能允许它只有一个副本呢？这样就存在单点故障，也就是如果该分区所在的集群宕机了的话，
我们的消费者就无法正常消费数据了。

解决方案: 在每一个broker对应的server-*.properties的配置文件中加入:
auto.create.topics.enable=true
default.replication.factor=3(你实际启动的broker数量)

参考: https://www.cnblogs.com/jun1019/p/6634545.html
