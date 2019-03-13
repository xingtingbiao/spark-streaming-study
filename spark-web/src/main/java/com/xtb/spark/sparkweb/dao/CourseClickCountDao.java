package com.xtb.spark.sparkweb.dao;

import com.xtb.spark.sparkweb.domain.CourseClickCount;
import com.xtb.spark.sparkweb.utils.HBaseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实战课程访问量数量数据访问层
 */
public class CourseClickCountDao {

    /**
     * 根据天查询HBase数据
     */
    public List<CourseClickCount> query(String tableName, String day) throws IOException {
        // 去HBase表中根据day去获取实战课程的点击数
        Map<String, Long> map = HBaseUtils.getInstance().query(tableName, day);
        return map.entrySet().stream()
                .map(x -> CourseClickCount.of().setName(x.getKey()).setValue(x.getValue()))
                .collect(Collectors.toList());
    }
}
