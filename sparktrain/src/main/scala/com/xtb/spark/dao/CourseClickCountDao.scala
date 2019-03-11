package com.xtb.spark.dao

import com.xtb.spark.domain.CourseClickCount

import scala.collection.mutable.ListBuffer

/**
  * 实战课程的点击数数据访问层
  */
object CourseClickCountDao {
  val TABLE_NAME = "imooc_course_clickcount"
  val CF = "info"
  val QUALIFIER = "click_count"


  /**
    * 保存数据到HBase
    * @param list  CourseClickCount集合
    */
  def save(list: ListBuffer[CourseClickCount]) = {

  }

  /**
    * 根据rowKey查询数据
    * @param day_course 就是rowKey的值 --> 20180502_130
    * @return 返回对应的20180502_130的访问总数
    */
  def count(day_course: String): Long = {

    0L
  }
}
