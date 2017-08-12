object VarianceDemo {

  def main(args: Array[String]): Unit = {
    // covariance 协变
    class T[+A] {}

    val x = new T[AnyRef]
    val y : T[Any] = x

    // val z : T[String] = x // type dismatch
  }

}
