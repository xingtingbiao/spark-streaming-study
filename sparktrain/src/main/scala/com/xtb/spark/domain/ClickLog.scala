package com.xtb.spark.domain


/**
  * 清洗后的日志信息
  * @param ip 日志访问的ip地址
  * @param time 日志访问的时间
  * @param courseId 日志访问的课程id
  * @param statusCode 日志访问的状态码
  * @param refer 日志访问的refer信息
  */
case class ClickLog(ip: String, time: String, courseId: Int, statusCode: Int, refer: String)
