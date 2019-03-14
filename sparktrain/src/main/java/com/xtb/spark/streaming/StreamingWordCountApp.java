package com.xtb.spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 使用Java编写sparkStreaming应用程序
 */
public class StreamingWordCountApp {

    public static void main(String[] args) throws InterruptedException {
        SparkConf sparkConf = new SparkConf().setAppName("StreamingWordCountApp").setMaster("local[2]");
        JavaStreamingContext jsc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
        JavaReceiverInputDStream<String> lines = jsc.socketTextStream("hadoop001", 9999);
        JavaPairDStream<String, Integer> result = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator())
                .mapToPair(x -> new Tuple2<>(x, 1))
                .reduceByKey((x, y) -> x + y);

        result.print();

        jsc.start();
        jsc.awaitTermination();
    }
}
