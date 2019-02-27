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
需求: 将统计结果写入到MySQL
详见代码: com.xtb.spark.streaming.ForeachRDDApp
CREATE TABLE `wordcount` (
`word`  varchar(64) NULL ,
`numb`  int(10) NULL 
);

通过插入的sql语句: 
"insert into wordcount(word, numb) values('" + record._1 + "'," + record._2 + ")"
存在的问题: 
a: 对于已有的数据没有做更新, 而是所有的数据都是做insert处理
    改进思路: 
        a) 在插入数据前先做判断单词是否存在, 如果存在就做update处理, 如果不存在就做insert处理
        b) 工作中: HBase/Redis 内部就有这样的API实现

b: 对于每个RDD中的每个partition都创建了一个conn, 可以优化成连接池实现




3) 基于window的统计
理解: 定时的进行一个时间段内数据处理
两个概念: 
   window length - The duration of the window (3 in the figure).  窗口的长度, 几个时间段
   sliding interval - The interval at which the window operation is performed (2 in the figure).  窗口的间隔

注意: 这两个参数和我们的batch size:[Seconds(5)]有关系, 整数倍的关系.

比如: 每隔多久计算某个范围内的数据: 每隔10秒计算前10分钟的wc
==> 每隔sliding interval统计前window length的值




4) 实战: 黑名单过滤





5) 实战: Spark Streaming整合Spark SQL实战





