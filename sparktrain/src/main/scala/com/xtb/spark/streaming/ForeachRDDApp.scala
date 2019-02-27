package com.xtb.spark.streaming

import java.sql.{Connection, DriverManager}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 使用Spark Streaming做词频统计, 并将结果写入到数据库
  */
object ForeachRDDApp {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("ForeachRDDApp")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.socketTextStream("hadoop001", 9999)
    val result = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)
    // result.print()   //此处仅仅是将结果输出到控制台
    // TODO... 将结果写到MySQL数据库中

    // 错误示例, conn对象序列化异常
    //    result.foreachRDD { rdd =>
    //      val conn = createConnection()   // executed at the driver
    //      rdd.foreach { record =>
    //        val sql = "insert into wordcount(word, numb) values('" + record._1 + "'," + record._2 + ")"
    //        conn.createStatement().execute(sql)    // executed at the worker
    //      }
    //    }

    // 通过分区创建conn解决driver端和worker端传递conn造成序列化异常, 减轻每一个record创建一个conn的压力
    // 这里的创建conn可以优化成connectionPool
    result.foreachRDD(_.foreachPartition { p =>
      val conn = createConnection()
      p.foreach { record =>
        val sql = "insert into wordcount(word, numb) values('" + record._1 + "'," + record._2 + ")"
        conn.createStatement().execute(sql)
      }
      conn.close()
    })

    ssc.start()
    ssc.awaitTermination()
  }

  def createConnection(): Connection = {
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql//localhost:3306/test", "root", "root")
  }

}
