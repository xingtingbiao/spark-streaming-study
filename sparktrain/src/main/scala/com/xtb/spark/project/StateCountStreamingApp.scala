package com.xtb.spark.project

import com.xtb.spark.domain.ClickLog
import com.xtb.spark.utils.DateUtils
import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  *  使用Spark Streaming处理Kafka过来的数据
  */
object StateCountStreamingApp {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: StateCountStreamingApp <brokers> <topics>")
      System.exit(1)
    }

    val Array(brokers, topics) = args
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("StateCountStreamingApp")
    val ssc = new StreamingContext(sparkConf, Seconds(60))

    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val topicsSet = topics.split(",").toSet

    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    // 测试步骤1: 测试数据接收
    // messages.map(_._2).count().print()

    // 测试步骤2: 数据清洗
    val logs = messages.map(_._2)
    val cleanData = logs.map(lines => {
      val infos = lines.split("\t")
      // info(2)  "GET /class/112.html HTTP/1.1"
      val url = infos(2).split(" ")(1)
      var courseId = 0
      if (url.startsWith("/class")) {
        val courseIdHtml = url.split("/")(2)
        courseId = courseIdHtml.substring(0, courseIdHtml.lastIndexOf(".")).toInt
      }
      ClickLog(infos(0), DateUtils.parseToMinute(infos(1)), courseId, infos(3).toInt, infos(4))
    }).filter(clickLog => clickLog.courseId != 0)

    cleanData.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
