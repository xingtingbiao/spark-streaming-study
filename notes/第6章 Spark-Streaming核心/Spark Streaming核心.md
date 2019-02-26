Spark Streaming核心
    1. 核心概念
    2. Transformations
    3. Output Operations
    4. 实战案例





1. 核心概念
StreamingContext***

def this(sparkContext: SparkContext, batchDuration: Duration) = {
  this(sparkContext, null, batchDuration)
}

def this(conf: SparkConf, batchDuration: Duration) = {
  this(StreamingContext.createNewSparkContext(conf), null, batchDuration)
}


batch interval 可以根据你的应用程序需求的延迟要求以及集群可用的资源情况来设置


一旦StreamingContext定义好之后, 就可以做一些事情


Points to remember(要注意的几点):

    Once a context has been started, no new streaming computations can be set up or added to it.
    Once a context has been stopped, it cannot be restarted.
    Only one StreamingContext can be active in a JVM at the same time.
    stop() on StreamingContext also stops the SparkContext. To stop only the StreamingContext, set the optional parameter of stop() called stopSparkContext to false.
    A SparkContext can be re-used to create multiple StreamingContexts, as long as the previous StreamingContext is stopped (without stopping the SparkContext) before the next StreamingContext is created.



DStream***
Discretized Streams (DStreams) 的简写形式
Internally, a DStream is represented by a continuous series of RDDs 实际上，DStream代表一个连续的RDD
Each RDD in a DStream contains data from a certain interval 每个RDD就是一个DStream里面的一个给定时间间隔内的数据

对DStream操作算子, 比如map/flatMap, 其实底层会被翻译为对DStream中的每个RDD都做相同的操作, 因为一个DStream是由不同批次的RDD所构成的
















