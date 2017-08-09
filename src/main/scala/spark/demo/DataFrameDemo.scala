package spark.demo

import org.apache.spark.sql.SparkSession

object DataFrameDemo {
  def main(args: Array[String]): Unit = {

    import org.apache.log4j.Logger
    import org.apache.log4j.Level
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    val spark = SparkSession.builder.appName("Spark DataFrame Demo").master("local[4]").getOrCreate()

    // For implicit conversions like converting RDDs to DataFrames
    import spark.implicits._
    val sc = spark.sparkContext

    val textFile = sc.textFile("src/main/resources/people.json")
    val df = textFile.toDF("line")

    df.show()

    val errors = df.filter($"line" like "%Andy%")

    println(errors.count())

    errors.show()

    val people = spark.read.json("src/main/resources/people.json")

    people.groupBy($"name").count().show()

    spark.stop()
  }
}
