package com.xtb.spark.project

import com.xtb.spark.dao.{CourseClickCountDao, CourseSearchClickCountDao}
import com.xtb.spark.domain.{ClickLog, CourseClickCount, CourseSearchClickCount}
import com.xtb.spark.utils.DateUtils
import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

/**
  * 使用Spark Streaming处理Kafka过来的数据
  */
object StateCountStreamingApp {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      System.err.println("Usage: StateCountStreamingApp <brokers> <topics>")
      System.exit(1)
    }

    val Array(brokers, topics) = args
    val sparkConf = new SparkConf() //.setMaster("local[4]").setAppName("StateCountStreamingApp") //服务器上运行时一般都注释
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

    // cleanData.print()

    // 测试步骤三: 统计今天到现在为止实战课程的访问量
    cleanData.map(x => {
      (x.time.substring(0, 8) + "_" + x.courseId, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
      rdd.foreachPartition(partition => {
        val list = new ListBuffer[CourseClickCount]
        partition.foreach(pair => {
          list.append(CourseClickCount(pair._1, pair._2))
        })
        CourseClickCountDao.save(list)
      })
    })

    // 测试步骤四: 统计今天到现在为止从搜索引擎引流过来的实战课程的访问量
    cleanData.filter(_.refer != "-").map(x => {
      // http://www.baidu.com/s?wd=Hadoop基础
      val host = x.refer.replaceAll("//", "/").split("/")(1)
      (x.time.substring(0, 8) + "_" + host + "_" + x.courseId, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
      rdd.foreachPartition(p => {
        val list = new ListBuffer[CourseSearchClickCount]
        p.foreach(pair => {
          list.append(CourseSearchClickCount(pair._1, pair._2))
        })
        CourseSearchClickCountDao.save(list)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
