package com.xtb.spark.sparkweb.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 实战课程访问量实体类
 */
@Data
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
public class CourseClickCount {
    private String name;
    private long value;
}
