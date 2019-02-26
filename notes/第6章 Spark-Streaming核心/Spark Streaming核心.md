Spark Streaming核心
    1. 核心概念
    2. Transformations
    3. Output Operations
    4. 实战案例





1. 核心概念
1) StreamingContext***

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



2) Input Dstreams and Receivers***

Every input DStream (except file stream, discussed later in this section) is associated with a Receiver (Scala doc, Java doc) object which receives the data from a source and stores it in Spark’s memory for processing.
每一个input DStream(除了文件系统) 都会关联一个从数据源接收数据的Receiver给Spark内存, 以供后续处理


3) Transformations***
详见图
Similar to that of RDDs, transformations allow the data from the input DStream to be modified.


4) Output Operations***
Output operations allow DStream’s data to be pushed out to external systems like a database or a file systems.




2. 案例实战之Spark Streaming处理socket数据
详见代码: com.xtb.spark.streaming.NetworkWordCount


3. 案例实战之Spark Streaming处理HDFS文件数据
详见代码: com.xtb.spark.streaming.FileWordCount
注意: 处理文件系统的数据的时候, 移动的文件要比app创建的晚才行

Note that

    The files must have the same data format.
    The files must be created in the dataDirectory by atomically moving or renaming them into the data directory.
    Once moved, the files must not be changed. So if the files are being continuously appended, the new data will not be read.

For simple text files, there is an easier method streamingContext.textFileStream(dataDirectory). And file streams do not require running a receiver, hence does not require allocating cores.










