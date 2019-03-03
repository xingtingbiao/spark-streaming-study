package com.xtb.spark.streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * SparkStreaming以Receiver方式的对接
  */
object KafkaReceiverWordCount {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      System.err.println("Usage: KafkaReceiverWordCount <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }

    val Array(zkQuorum, group, topics, numThreads) = args
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("KafkaReceiverWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap

    // TODO... SparkStreaming以Receiver方式的对接Kafka
    val messages = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)

    // TODO... 测试打印出messages的数据结构
    messages.foreachRDD(_.take(1).foreach(println))
    messages.map(_._2).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
