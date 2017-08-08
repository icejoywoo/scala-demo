package spark.demo

import org.apache.spark.sql.SparkSession

object PiEstimation {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("Spark Pi").master("local[4]").getOrCreate()
    val sc = spark.sparkContext

    val NUM_SAMPLES = Int.MaxValue

    // Spark can also be used for compute-intensive tasks. This code estimates π by "throwing darts" at a circle.
    // We pick random points in the unit square ((0, 0) to (1,1)) and see how many fall in the unit circle.
    // The fraction should be π / 4, so we use this to get our estimate.
    val count = sc.parallelize(1 to NUM_SAMPLES).filter { _ =>
      val x = math.random
      val y = math.random
      x * x + y * y < 1
    }.count()

    println(s"Pi is roughly ${4.0 * count / NUM_SAMPLES}")

    spark.stop()
  }
}
