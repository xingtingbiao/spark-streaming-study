package com.xtb.spark.streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 黑名单过滤实战
  */
object TransformApp {
  Logger.getLogger("org").setLevel(Level.WARN)

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("TransformApp")
    // 创建StreamingContext需要两个参数: SparkConf和batch interval
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    //构建黑名单
    val blacks = List("zs", "ls")
    // 这里有坑x不能用'_'通配符
    val blackRDD = ssc.sparkContext.parallelize(blacks).map(x => (x, true))

    // Linux命令: nc -lk 9999
    val lines = ssc.socketTextStream("hadoop001", 9999)
    val value = lines.map(x => (x.split(",")(1), x)).transform(rdd => {
      val v = rdd.leftOuterJoin(blackRDD)
      // 输出结构 --> (ls,(20180808,ls,Some(true)))
      v.take(1).foreach(println)
      v.filter(x => !x._2._2.getOrElse(false))
        .map(x => x._2._1)
    })
    value.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
