package com.xtb.spark.offset

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Kafka对接SparkStreaming的offset管理
  */
object Offset01App {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Offset01App").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> "hadoop001:9092",
      "auto.offset.reset" -> "smallest"
    )
    val topics = "xtb_offset".split(",").toSet
    val message = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

    message.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        println("慕课XTB: " + rdd.count())
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
