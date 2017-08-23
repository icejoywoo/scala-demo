package fpinscala.exercises.chapter4

object OptionDemo {

  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  def variance(xs: Seq[Double]): Option[Double] = mean(xs) flatMap (m => mean(xs.map(x => math.pow(x - m, 2))))

  def Try[A](a: => A): Option[A] =
    try Some(a)
    catch {
      case e: Exception => None
    }

  def main(args: Array[String]): Unit = {
    val x = 1
    // throw exception 是任意类型
//    x + ((throw new Exception("fail")): Int)

    println(mean(Seq(5, 3, 4)).getOrElse(-1))
    println(variance(Seq(5, 3, 4)))

    val abs0: Option[Double] => Option[Double] = Option.lift(math.abs)

    def f(a: Int, b: Int): Int = a + b
    println(Option.map2(Some(1), None)(f))
    println(Option.map2(Some(1), Some(2))(f))

    println(Option.sequence_1(List("1", "2", "3") map (Try(_))))

    println(Option.sequence(List("1", "2", "3").map(x => Try(x.toInt))))
    println(Option.sequence(List("1", "abc", "3").map(x => Try(x.toInt))))


  }
}
