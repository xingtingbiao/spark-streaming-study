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






