package com.xtb.spark.streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 使用Spark Streaming做有状态的词频统计
  */
object StatefulWordCount {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("StatefulWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    // 如果使用stateful的算子, 必须要设置checkpoint
    // 建议在生产环境中, 把checkpoint设置到HDFS的某个文件夹中
    ssc.checkpoint(".")
    val lines = ssc.socketTextStream("hadoop001", 9999)
    val result = lines.flatMap(_.split(" ")).map((_, 1))
    // 这里的隐式转换 _去掉貌似也可以
    val state = result.updateStateByKey(updateFunction _)
    state.print()

    ssc.start()
    ssc.awaitTermination()
  }

  def updateFunction(currentValues: Seq[Int], preValues: Option[Int]): Option[Int] = {
    val current = currentValues.sum
    val pre = preValues.getOrElse(0)
    Some(current + pre)
  }
}
