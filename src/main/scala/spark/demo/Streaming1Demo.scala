package spark.demo

import org.apache.spark._
import org.apache.spark.streaming._

object Streaming1Demo {
  def main(args: Array[String]): Unit = {

    import org.apache.log4j.Logger
    import org.apache.log4j.Level
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    // shell 启动
    // $ nc -lk 9999
    // 输入数据，作为本示例的流式输入

    // 首先配置一下本 quick example 将跑在本机，app name 是 NetworkWordCount
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    // batchDuration 设置为 1 秒，然后创建一个 streaming 入口
    val ssc = new StreamingContext(conf, Seconds(1))

    // ssc.socketTextStream() 将创建一个 SocketInputDStream；这个 InputDStream 的 SocketReceiver 将监听本机 9999 端口
    val lines = ssc.socketTextStream("localhost", 9999)

    val words = lines.flatMap(_.split(" "))      // DStream transformation
    val pairs = words.map(word => (word, 1))     // DStream transformation
    val wordCounts = pairs.reduceByKey(_ + _)    // DStream transformation
    wordCounts.print()                           // DStream output
    // 上面 4 行利用 DStream transformation 构造出了 lines -> words -> pairs -> wordCounts -> .print() 这样一个 DStreamGraph
    // 但注意，到目前是定义好了产生数据的 SocketReceiver，以及一个 DStreamGraph，这些都是静态的

    // 下面这行 start() 将在幕后启动 JobScheduler, 进而启动 JobGenerator 和 ReceiverTracker
    // ssc.start()
    //    -> JobScheduler.start()
    //        -> JobGenerator.start();    开始不断生成一个一个 batch
    //        -> ReceiverTracker.start(); 开始往 executor 上分布 ReceiverSupervisor 了，也会进一步创建和启动 Receiver
    ssc.start()

    // 然后用户 code 主线程就 block 在下面这行代码了
    // block 的后果就是，后台的 JobScheduler 线程周而复始的产生一个一个 batch 而不停息
    // 也就是在这里，我们前面静态定义的 DStreamGraph 的 print()，才一次一次被在 RDD 实例上调用，一次一次打印出当前 batch 的结果
    ssc.awaitTermination()
  }
}
