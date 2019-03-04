package com.xtb.spark.streaming

import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf

/**
  * SparkStreaming以Direct方式的对接
  */
object KafkaDirectWordCount {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: KafkaDirectWordCount <brokers> <topics>")
      System.exit(1)
    }

    val Array(brokers, topics) = args
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("KafkaDirectWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val topicsSet = topics.split(",").toSet

    // TODO... SparkStreaming以Direct方式的对接Kafka
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    // TODO... 测试打印出messages的数据结构
    // messages.foreachRDD(_.take(1).foreach(println))
    messages.map(_._2).flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    ssc.start()
    ssc.awaitTermination()
  }
}
