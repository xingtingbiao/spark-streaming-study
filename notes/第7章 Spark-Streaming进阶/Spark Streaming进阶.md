Spark Streaming进阶

1. 带状态的算子: UpdateStateByKey
2. 实战: 计算到目前为止累计出现的单词个数写入到MySQL中
3. 基于window的统计
4. 实战: 黑名单过滤
5. 实战: Spark Streaming整合Spark SQL实战




1) 带状态的算子: UpdateStateByKey
The updateStateByKey operation allows you to maintain arbitrary state while continuously updating it with new information. To use this, you will have to do two steps.

    Define the state - The state can be an arbitrary data type.
    Define the state update function - Specify with a function how to update the state using the previous state and the new values from an input stream.

需求: 统计到目前为止累计出现的单词个数(需要保持住以前的状态)
详见代码: com.xtb.spark.streaming.StatefulWordCount
requirement failed: The checkpoint directory has not been set. Please set it by StreamingContext.checkpoint().
如果使用stateful的算子, 必须要设置checkpoint




2) 实战: 计算到目前为止累计出现的单词个数写入到MySQL中





3) 基于window的统计





4) 实战: 黑名单过滤





5) 实战: Spark Streaming整合Spark SQL实战





