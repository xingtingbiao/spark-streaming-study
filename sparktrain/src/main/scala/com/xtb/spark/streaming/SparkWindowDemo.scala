package com.xtb.spark.streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkWindowDemo {
  Logger.getLogger("org").setLevel(Level.WARN)
  def main(args: Array[String]): Unit = {
    val parkConf = new SparkConf().setMaster("local[2]").setAppName("window")
    // 这里的batch size时长必须是下面窗口时间的约数
    val ssc = new StreamingContext(parkConf, Seconds(3))
    ssc.checkpoint(".")
    val ds = ssc.socketTextStream("hadoop001", 9999)
    //Seconds(20)表示窗口的宽度   Seconds(10)表示多久滑动一次(滑动的时间长度)
    val re = ds.flatMap(_.split(" ")).map((_, 1)).reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(20), Seconds(10))
    //    窗口长度和滑动的长度一致,那么类似于每次计算自己批量的数据,用updateStateByKey也可以累计计算单词的wordcount 这里只是做个是实验
    //    val re = ds.flatMap(_.split(" ")).map((_, 1)).reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(4), Seconds(4)).updateStateByKey(myFunc, new HashPartitioner(sc.defaultParallelism), true)
    re.print()
    ssc.start()
    ssc.awaitTermination()
  }

  val myFunc = (it: Iterator[(String, Seq[Int], Option[Int])]) => {
    it.map(x => {
      (x._1, x._2.sum + x._3.getOrElse(0))
    })
  }

}
