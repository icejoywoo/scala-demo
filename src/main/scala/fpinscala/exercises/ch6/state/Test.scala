package fpinscala.exercises.ch6.state

import RNG._

object Test {

  // ex 6.11
  sealed trait Input
  case object Coin extends Input
  case object Turn extends Input

  case class Machine(locked: Boolean, candies: Int, coins: Int)

  // 不会
  def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] = ???

  def main(args: Array[String]): Unit = {
    val r = Simple(1)
    assert(sequence(List(unit(1), unit(2), unit(3)))(r)._1 == List(1, 2, 3))

    println(ints(5)(r))

    println(nonNegativeLessThan(10)(r))
  }
}
