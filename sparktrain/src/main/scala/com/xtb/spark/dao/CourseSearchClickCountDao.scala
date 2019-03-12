package com.xtb.spark.dao

import com.xtb.spark.domain.{CourseClickCount, CourseSearchClickCount}
import com.xtb.spark.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/**
  * 实战课程的点击数数据访问层
  */
object CourseSearchClickCountDao {
  val TABLE_NAME = "imooc_course_search_clickcount"
  val CF = "info"
  val QUALIFIER = "click_count"


  /**
    * 保存数据到HBase
    * @param list CourseSearchClickCount集合
    */
  def save(list: ListBuffer[CourseSearchClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(TABLE_NAME)
    for (ele <- list) {
      // incrementColumnValue这个API是增量列值的意思, 将value数值进行累加
      table.incrementColumnValue(Bytes.toBytes(ele.day_search_course), Bytes.toBytes(CF), Bytes.toBytes(QUALIFIER), ele.click_count)
    }
  }

  /**
    * 根据rowKey查询数据
    * @param day_course 就是rowKey的值 --> 20180502_www.baidu.com_130
    * @return 返回对应的20180502_www.baidu.com_130的访问总数
    */
  def count(day_course: String): Long = {
    val table = HBaseUtils.getInstance().getTable(TABLE_NAME)
    val get = new Get(Bytes.toBytes(day_course))
    val value = table.get(get).getValue(CF.getBytes(), QUALIFIER.getBytes)
    if (null == value) {
      0L
    } else {
      Bytes.toLong(value)
    }
  }

  def main(args: Array[String]): Unit = {
    val list = new ListBuffer[CourseSearchClickCount]
    list.append(CourseSearchClickCount("20171111_www.baidu.com_8", 8))
    list.append(CourseSearchClickCount("20171111_cn.bing.com_9", 9))
    save(list)
    println(count("20171111_www.baidu.com_8") + " : " + count("20171111_cn.bing.com_9"))
  }
}
