package com.xtb.spark.streaming;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * 使用Java开发Spark应用程序
 */
public class WordCountApp {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("WordCountApp").master("local[2]").getOrCreate();

        // TODO... 处理具体的业务逻辑数据
        JavaRDD<String> lines = spark.read().textFile("hello.txt").javaRDD();
        JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split("\t")).iterator());
        JavaPairRDD<String, Integer> counts = words.mapToPair(word -> new Tuple2<>(word, 1)).reduceByKey((x, y) -> x + y);
        List<Tuple2<String, Integer>> list = counts.collect();

        for (Tuple2 tuple : list) {
            System.out.println(tuple._1 + " : " + tuple._2);
        }

        spark.stop();
    }
}
