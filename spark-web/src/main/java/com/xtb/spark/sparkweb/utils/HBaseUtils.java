package com.xtb.spark.sparkweb.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    HTable getTable(String tableName) {
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
     * @param rowKey    HBase表的rowKey
     * @param cf        HBase表的column family
     * @param column    HBase表的列
     * @param value     写入HBase表的值
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

    /**
     * 根据表名和输入条件获取HBase的记录数
     * @param tableName 具体的表名
     * @param condition 输入条件--> rowKey的前缀
     * @return 返回记录数的map
     */
    public Map<String, Long> query(String tableName, String condition) throws IOException {
        Map<String, Long> map = new HashMap<>();
        HTable table = getTable(tableName);
        String cf = "info";
        String qualifier = "click_count";
        Scan scan = new Scan();
        Filter filter = new PrefixFilter(Bytes.toBytes(condition));
        scan.setFilter(filter);
        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            String row = Bytes.toString(result.getRow());
            long clickCount = Bytes.toLong(result.getValue(cf.getBytes(), qualifier.getBytes()));
            map.put(row, clickCount);
        }
        return map;
    }

}
