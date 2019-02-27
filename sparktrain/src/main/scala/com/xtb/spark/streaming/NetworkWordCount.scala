package com.xtb.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.log4j.{Level, Logger}

/**
  * Spark Streaming 处理Socket数据
  */
object NetworkWordCount {
  Logger.getLogger("org").setLevel(Level.WARN)
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    // 创建StreamingContext需要两个参数: SparkConf和batch interval
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    // Linux命令: nc -lk 9999
    val lines = ssc.socketTextStream("hadoop001", 9999)
    val results = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    results.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
