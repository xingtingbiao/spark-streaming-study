package com.xtb.spark.sparkweb.utils;

import java.util.HashMap;
import java.util.Map;

public class BaseData {
//    public static final Map<String, String> COURSE_MAP = new HashMap<String, String>(){{
//        put("112", "");
//        put("128", "");
//        put("145", "");
//        put("146", "");
//        put("131", "");
//        put("130", "");
//    }};

    public static final Map<String, String> COURSE_MAP = new HashMap<>();

    static {
        COURSE_MAP.put("112", "Spark SQL慕课网日志分析");
        COURSE_MAP.put("128", "10小时入门大数据");
        COURSE_MAP.put("145", "深度学习之神经网络核心原理与算法");
        COURSE_MAP.put("146", "强大的Node.js在Web开发的应用");
        COURSE_MAP.put("131", "Vue+Django实战");
        COURSE_MAP.put("130", "Web前端性能优化");
    }
}
