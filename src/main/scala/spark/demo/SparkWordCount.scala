package spark.demo

import org.apache.spark.{SparkConf, SparkContext}

object SparkWordCount {
  def main(args: Array[String]) {
    // create Spark context with Spark configuration
    val sc = new SparkContext(new SparkConf().setAppName("Spark Count").setMaster("local[2]"))

    // get threshold
    val threshold = 1

    // read in text file and split each document into words
    val tokenized = sc.textFile("./pom.xml").flatMap(_.split(" "))

    // count the occurrence of each word
    val wordCounts = tokenized.map((_, 1)).reduceByKey(_ + _)

    // filter out words with fewer than threshold occurrences
    val filtered = wordCounts.filter(_._2 >= threshold)

    // count characters
    val charCounts = filtered.flatMap(_._1.toCharArray).map((_, 1)).reduceByKey(_ + _)

    System.out.println(charCounts.collect().mkString(", "))

    // 挂住 10 分钟; 这时可以去看 SparkUI: http://localhost:4040
    Thread.sleep(10 * 60 * 1000)
  }
}
