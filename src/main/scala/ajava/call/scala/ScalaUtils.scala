package ajava.call.scala

object ScalaUtils {
  def log(msg: String): Unit = Console.println(msg)

  val MAX_LOG_SIZE = 1056
}

object FunctionUtil {
  def testFunction(f: Int => Int): Int = f(5)
}

abstract class AbstractFunctionIntIntForJava extends (Int => Int) {}
