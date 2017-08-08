package scala.call.java

object SimpleJavaClassInvokedInScala {
  def main(args: Array[String]): Unit = {
    val s = SimpleJavaClass.create("test")
    println(s)
  }
}
