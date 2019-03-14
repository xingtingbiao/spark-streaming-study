package com.xtb.spark.offset

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

/**
  * Kafka对接SparkStreaming的offset管理
  */
object Offset02App {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Offset02App").setMaster("local[2]")


    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> "hadoop001:9092",
      "auto.offset.reset" -> "smallest"
    )
    val topics = "xtb_offset".split(",").toSet
    val checkpointDirectory = "hdfs://hadoop001:8020/offset"

    // Function to create and setup a new StreamingContext
    def functionToCreateContext(): StreamingContext = {
      // new context
      val ssc = new StreamingContext(sparkConf, Seconds(10))
      // create DStreams
      val message = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
      // set checkpoint directory
      ssc.checkpoint(checkpointDirectory)
      message.checkpoint(Duration(10 * 1000))

      message.foreachRDD(rdd => {
        if (!rdd.isEmpty()) {
          println("慕课XTB: " + rdd.count())
        }
      })

      ssc
    }

    val ssc = StreamingContext.getOrCreate(checkpointDirectory, functionToCreateContext _)

    ssc.start()
    ssc.awaitTermination()
  }

}
