package com.xtb.spark.utils

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

/**
  * 时间解析工具
  */
object DateUtils {

  val ORIGIN_FORMAT: FastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  val TARGET_FORMAT: FastDateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss")

  def getTime(time: String): Long = {
    try {
      ORIGIN_FORMAT.parse(time).getTime
    } catch {
      case e: Exception =>
        println(e.getMessage)
        0L
    } finally {
      0L
    }
  }

  def parseToMinute(time: String): String = {
    TARGET_FORMAT.format(new Date(getTime(time)))
  }


  def main(args: Array[String]): Unit = {
    println(parseToMinute("2019-03-11 22:27:01"))
  }
}
