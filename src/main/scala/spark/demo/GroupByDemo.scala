package spark.demo

import org.apache.spark.sql.SparkSession

object GroupByDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Spark DataFrame Demo").master("local[4]").getOrCreate()

    val df = spark.read.format("json").load("src/main/resources/people.json")

    df.show()

    df.groupBy()

    spark.close()
  }
}
