package fpinscala.exercises

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def apply[A](as: A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
}

object chapter3 {

  // ex 3.2
  def tail[A](l: List[A]): List[A] = l match {
    case Cons(x, xs) => xs
    case Nil => Nil
  }

  // ex 3.3
  def setHead[A](l: List[A], head: A): List[A] = l match {
    case Cons(x, xs) => Cons(head, xs)
    case Nil => Nil
  }

  // ex 3.4
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Cons(x, xs) => if (f(x)) Cons(x, dropWhile(xs, f)) else dropWhile(xs, f)
    case Nil => Nil
  }

  def main(args: Array[String]): Unit = {
    println(List(1, 2, 3))

    val ex1: List[Double] = Nil
    val ex2: List[Int] = Cons(1, Nil)
    val ex3: List[String] = Cons("a", Cons("b", Nil))

    // ex 3.1
    val x = List(1, 2, 3, 4, 5) match {
      case Cons(x, Cons(2, Cons(4, _))) => x
      case Nil => 42
      case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
      case Cons(h, t) => h + List.sum(t)
      case _ => 101
    }
    assert(x == 3)

    println(tail(List(1, 2, 3)))
    println(tail(Nil))
    println(setHead(List(1, 2, 3), 4))
    println(dropWhile(List(1, 2, 3, 4, 5, 6), (x: Int) => x > 3))
  }
}
