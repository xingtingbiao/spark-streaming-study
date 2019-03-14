SparkStreaming实战补充


1. The semantics of streaming systems

2. Kafka Offset Management with Spark Streaming

3. Output operations ensure at-least once semantics (最好是 Exactly-once semantics)




一. 流处理系统的语义

The semantics of streaming systems are often captured in terms of how many times each record can be processed by the system. There are three types of guarantees that a system can provide under all possible operating conditions (despite failures, etc.)

    1. At most once: Each record will be either processed once or not processed at all.
    2. At least once: Each record will be processed one or more times. This is stronger than at-most once as it ensure that no data will be lost. But  
       there may be duplicates.
    3. Exactly once: Each record will be processed exactly once - no data will be lost and no data will be processed multiple times. This is obviously 
       the strongest guarantee of the three.


流处理系统常见的三种语义
1) 最多一次
2) 至少一次
3) 有且仅有一次 *****



