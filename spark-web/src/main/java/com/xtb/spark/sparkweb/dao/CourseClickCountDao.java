package com.xtb.spark.sparkweb.dao;

import com.xtb.spark.sparkweb.domain.CourseClickCount;
import com.xtb.spark.sparkweb.utils.BaseData;
import com.xtb.spark.sparkweb.utils.HBaseUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 实战课程访问量数量数据访问层
 */
@Component
public class CourseClickCountDao {

    /**
     * 根据天查询HBase数据
     */
    public List<CourseClickCount> query(String tableName, String day) throws IOException {
        // 去HBase表中根据day去获取实战课程的点击数
        Map<String, Long> map = HBaseUtils.getInstance().query(tableName, day);
        return map.entrySet().stream()
                .map(x -> {
                    // 这里默认构造的数据都是带'_'的标准数据
                    String name = BaseData.COURSE_MAP.get(x.getKey().split("_")[1]);
                    CourseClickCount clickCount = null;
                    if (!StringUtils.isEmpty(name)) {
                        clickCount = CourseClickCount.of().setName(name).setValue(x.getValue());
                    }
                    return clickCount;
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
