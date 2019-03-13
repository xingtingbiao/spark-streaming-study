package com.xtb.spark.sparkweb.dao;

import com.xtb.spark.sparkweb.domain.CourseClickCount;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CourseClickCountDaoTests {

    @Test
    public void testQuery() throws IOException {
        List<CourseClickCount> list = new CourseClickCountDao().query("imooc_course_clickcount", "20190312");
        for (CourseClickCount count : list) {
            System.out.println("rowKey: " + count.getName() + "  value: " + count.getValue());
        }
    }
}
