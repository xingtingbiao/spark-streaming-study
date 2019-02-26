package com.xtb.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.log4j.{Level, Logger}

object FileWordCount {
  Logger.getLogger("org").setLevel(Level.WARN)
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("FileWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val lines = ssc.textFileStream("D:/test/test/")
    val results = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    results.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
