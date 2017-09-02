package fpinscala.exercises.ch7.parallelism

import java.util.concurrent.Executors

object Demo {
  def main(args: Array[String]): Unit = {
    val S = Executors.newFixedThreadPool(4)
    val echoer = Actor[String](S) {
      msg => println(s"Got message: '$msg'")
    }

    echoer ! "hello"
  }
}
