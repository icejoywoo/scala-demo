package spark.demo

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StringType, StructField, StructType, TimestampType}

object GroupByDemo {
  def main(args: Array[String]): Unit = {
    // disable log
    import org.apache.log4j.Logger
    import org.apache.log4j.Level
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    val spark = SparkSession.builder.appName("Spark DataFrame Demo").master("local[4]").getOrCreate()

    import spark.implicits._
    import org.apache.spark.sql.functions._

    val df = spark.read.format("json").load("src/main/resources/cities.json")

    df.show()
    println(df.schema)

    df.groupBy($"name")
      .count()
      .show()

    val schemaExp = StructType(
      StructField("timestamp", TimestampType, false) ::
        StructField("name", StringType, false) ::
        StructField("city", StringType, true)
        :: Nil
    )

    val df1 = spark.read.schema(schemaExp).json("src/main/resources/cities.json")

    df1.groupBy(
      // timeColumn, windowDuration, slideDuration, startTime
      window($"timestamp", "10 seconds", "5 seconds"),
      $"name")
      .count()
      .show(20, 100)

    df1.groupBy(
      window($"timestamp", "10 seconds"),
      $"name")
      .count()
      .show(20, 100)

    spark.close()
  }
}
