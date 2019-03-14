package com.xtb.spark.offset

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

/**
  * 1) 创建StreamingContext
  * 2) 从Kafka中获取数据   <== offset获取
  * 3) 根据业务逻辑进行处理
  * 4) 将处理结果写入到外部存储中去   <== offset保存
  * 5) 启动程序, 等待程序终止
  */
object Offset03App {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Offset03App").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> "hadoop001:9092",
      "auto.offset.reset" -> "smallest"
    )
    val topics = "xtb_offset".split(",").toSet

    /**
      * TODO... 获取偏移量
      * MySQL/ZK...
      */
    val fromOffsets = Map[TopicAndPartition, Long]()
    val message = if (fromOffsets.nonEmpty) { // 不为空且还要找到对应key的value值
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    } else {
      val messageHandler = (mm: MessageAndMetadata[String, String]) => (mm.key(), mm.message())
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffsets, messageHandler)
    }


    message.foreachRDD(rdd => {
      // 假设为具体业务逻辑
      println("慕课XTB: " + rdd.count())

      /**
        * 将offset提交
        */
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      for (o <- offsetRanges) {
        // 这里处理具体的偏移量offset 保存到外部存储中 ==> Mysql/ZK/Redis...
        println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
