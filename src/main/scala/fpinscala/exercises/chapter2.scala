package fpinscala.exercises

import scala.annotation.tailrec

object chapter2 {

  def factorial(n : Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int = {
      if (n <= 0) acc
      else go(n-1, n * acc)
    }
    go(n, 1)
  }

  // ex 2.1
  def fib_notr(n: Int): Int = {
      if (n == 0 || n == 1) n
      else fib_notr(n-1) + fib_notr(n-2)
  }

  def fib(n: Int): Int = {
    def go(n: Int, acc1: Int, acc2: Int): Int = {
      if (n < 2) acc1
      else go(n-1, acc2, acc1+acc2)
    }

    go(n, 1, 1)
  }

  // 2.5

  def findFirst[A](as: Array[A], p: A => Boolean): Int = {
    def loop(n: Int): Int = {
      if (n >= as.length) -1
      else if (p(as(n))) n
      else loop(n+1)
    }

    loop(0)
  }

  // ex 2.2
  def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
    def loop(n: Int): Boolean = {
      if (n+1 >= as.length) true
      else if (ordered(as(n), as(n+1))) loop(n+1)
      else false
    }

    loop(0)
  }

  // 2.6
  def partial1[A, B, C](a: A, f: (A, B) => C): B => C =
    (b: B) => f(a, b)

  // ex 2.3
  def curry[A, B, C](f: (A, B) => C): A => (B => C) =
    a => b => f(a, b)

  // ex 2.4
  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a, b) => f(a)(b)

  // ex 2.5
  def compose[A, B, C](f: B => C, g: A=> B): A => C =
    (a) => f(g(a))

  def main(args: Array[String]): Unit = {
    assert(factorial(5) == 120)
    assert(fib_notr(20) == 6765)
    assert(fib(20) == 6765)

    assert(findFirst(Array("a", "b", "c"), (x: String) => x == "c") == 2)
    assert(findFirst(Array("a", "b", "c"), (x: String) => x == "d") == -1)

    assert(findFirst(Array(1, 2, 3), (x: Int) => x == 4) == -1)

    assert(isSorted(Array(1, 2, 3), (x: Int, y: Int) => x <= y))
    assert(isSorted(Array(1, 3, 3), (x: Int, y: Int) => x <= y))
    assert(!isSorted(Array(1, 3, 2), (x: Int, y: Int) => x <= y))

    assert(partial1(5, (x: Int, y: Int) => x + y)(5) == 10)

    assert(curry((x: Int, y: Int) => x + y)(4)(4) == 8)
    assert(uncurry((x: Int) => (y: Int) => x + y)(4, 4) == 8)
    assert(compose((x: String) => x + "c", (y: String) => y + "b")("a") == "abc")

    val f = (x: String) => x + "c"
    val g = (y: String) => y + "b"
    assert((f compose g)("a") == "abc")
    assert((g andThen f)("a") == "abc")
  }

}
