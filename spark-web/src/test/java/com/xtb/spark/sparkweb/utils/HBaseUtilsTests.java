package com.xtb.spark.sparkweb.utils;

import org.apache.hadoop.hbase.client.HTable;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class HBaseUtilsTests {

    @Test
    public void testGetTableName() {
        HTable table = HBaseUtils.getInstance().getTable("imooc_course_clickcount");
        System.out.println(table.getName().getNameAsString());
    }

    @Test
    public void testPutValue() {
        String tableName = "imooc_course_clickcount";
        String rowKey = "20181111_88";
        String cf = "info";
        String column = "click_count";
        String value = "30";
        HBaseUtils.getInstance().put(tableName, rowKey, cf, column, value);
    }

    @Test
    public void testQueryValue() throws IOException {
        String tableName = "imooc_course_clickcount";
        String condition = "20190312";
        Map<String, Long> query = HBaseUtils.getInstance().query(tableName, condition);
        for (Map.Entry entry : query.entrySet()) {
            System.out.println("rowKey: " + entry.getKey() + "  value: " + entry.getValue());
        }
    }
}
