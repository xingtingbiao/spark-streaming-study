package com.xtb.spark.domain

/**
  * 实战课程的点击数实体类
  * @param day_course   对应的就是HBase的rowKey --> 20180502_130
  * @param click_count  对应的20180502_130的访问总数
  */
case class CourseClickCount(day_course: String, click_count: Long)
