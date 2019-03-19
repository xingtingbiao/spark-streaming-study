Spark-Streaming入门

1. 概述                                4. 发展史
2. 应用场景                            5. 从词频统计功能着手入门
3. 集成Spark生态系统的使用             6. 工作原理



1. 概述
    Spark Streaming is an extension of the core Spark API that enables scalable, high-throughput, fault-tolerant stream processing of live data streams.

Spark-Streaming的定义:
    将不同的数据源的数据经过Spark-Streaming处理之后将结果输出到外部文件系统


特点: 
    低延迟
    能从错误中高效的恢复: fault-tolerant
    能够运行在成百上千的节点上
    能够将批处理、机器学习、图计算等子框架和Spark Streaming综合起来使用


Spark Streaming是否需要独立安装?
    不需要!!!
    One stack to rule them all:  一栈式解决




2. Spark Streaming应用场景
    详见Spark-Streaming应用场景的例子.png
    实时推荐
    日志监控



3. 集成Spark生态系统的使用  
   详见图



4. 发展史
   详见图



5. 从词频统计功能着手入门
    1. spark-submit执行(生产)
spark-submit --master local[2] \
--class org.apache.spark.examples.streaming.NetworkWordCount \
--name NetworkWordCount \
/home/xingtb/app/spark-2.2.3-bin-2.6.0-cdh5.7.0/examples/jars/spark-examples_2.11-2.2.3.jar hadoop001 9999

    2. spark-shell执行(测试)
        spark-shell --master local[2]

    直接copy源码
import org.apache.spark.streaming.{Seconds, StreamingContext}
val ssc = new StreamingContext(sc, Seconds(1))
val lines = ssc.socketTextStream("hadoop001", 9999)
val words = lines.flatMap(_.split(" "))
val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
wordCounts.print()
ssc.start()
ssc.awaitTermination()


github源码地址: https://github.com/apache/spark


补充: $SPARK_HOME/conf/log4j.properties  
中将log4j.rootCategory=INFO, console 改成 log4j.rootCategory=WARN, console 可以屏蔽info级别的日志





6. 工作原理
(1) Spark Streaming工作原理(粗粒度)
    详见图
    Spark Streaming接收到实时数据流, 把数据按照指定的时间段切成一片片小的数据块, 
    然后把小的数据块传给Spark Engine处理。


(2) Spark Streaming工作原理(细粒度)
    详见图











