package com.xtb.spark.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * HBase操作的工具类, Java工具类建议采用单例模式封装
 */
public class HBaseUtils {
    private HBaseAdmin admin = null;
    private Configuration configuration;

    /**
     * 私有构造方法
     */
    private HBaseUtils() {
        configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum", "hadoop001:2181");
        configuration.set("hbase.rootdir", "hdfs://hadoop001:8020/hbase");

        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HBaseUtils instance = null;

    public static synchronized HBaseUtils getInstance() {
        if (null == instance) {
            return new HBaseUtils();
        }
        return instance;
    }

    public HTable getTable(String tableName) {
        HTable hTable = null;
        try {
            hTable = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hTable;
    }

    /**
     * 添加一条数据到HBase表中
     * @param tableName HBase表名
     * @param rowKey HBase表的rowKey
     * @param cf   HBase表的column family
     * @param column  HBase表的列
     * @param value  写入HBase表的值
     */
    public void put(String tableName, String rowKey, String cf, String column, String value) {
        HTable table = getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        HTable table = HBaseUtils.getInstance().getTable("imooc_course_clickcount");
//        System.out.println(table.getName().getNameAsString());
        String tableName = "imooc_course_clickcount";
        String rowKey = "20181111_88";
        String cf = "info";
        String column = "click_count";
        String value = "30";
        HBaseUtils.getInstance().put(tableName, rowKey, cf, column, value);
    }
}
