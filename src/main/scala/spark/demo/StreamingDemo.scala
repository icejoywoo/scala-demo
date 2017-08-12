package spark.demo

import java.util.concurrent.TimeUnit

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types._

object StreamingDemo {
  def main(args: Array[String]): Unit = {

    import org.apache.log4j.{Level, Logger}
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    val spark = SparkSession.builder.
      master("local[2]").
      appName("StructuredNetworkWordCount").
      getOrCreate()

    import org.apache.spark.sql.functions._
    import spark.implicits._

    val schemaExp = StructType(
      StructField("timestamp", TimestampType, false) ::
        StructField("name", StringType, false) ::
        StructField("city", StringType, true)
        :: Nil
    )

    //标准的DataSource API，只不过read变成了readStream
    val words = spark.readStream.format("json").schema(schemaExp)
      .load("file:///tmp/input")

    // DataFrame 的一些API
    //    val wordCounts = words.groupBy("name").count()
    val wordCounts = words
      .withWatermark("timestamp", "10 seconds")
      .groupBy(
        window($"timestamp", "10 seconds", "5 seconds"),
        $"name")
      .count()

    //标准的DataSource 写入 API，只不过write变成了writeStream
    val query = wordCounts.writeStream
      //complete,append,update。目前只
      //支持前面两种
      .outputMode("complete")
      //console,parquet,memory,foreach 四种
      .format("console")
      .trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS)) //这里就是设置定时器了
      .start()

    query.awaitTermination()
  }
}
