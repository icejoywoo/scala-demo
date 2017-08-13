package fpinscala.exercises

import scala.annotation.tailrec

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
  def drop[A](l: List[A], n: Int): List[A] = {
    if (n == 0) l
    else l match {
      case Cons(x, xs) => drop(xs, n-1)
      case Nil => Nil
    }
  }

  // ex 3.5
  def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
    case Cons(x, xs) => if (f(x)) Cons(x, dropWhile(xs, f)) else dropWhile(xs, f)
    case Nil => Nil
  }

  // currying 的写法，可以推导出后面 f 的类型，在调用的时候不需要表明 f 的输入参数类型
  def dropWhile2[A](l: List[A])(f: A => Boolean): List[A] =  l match {
    case Cons(x, xs) if f(x) => dropWhile2(xs)(f)
    case _ => l
  }

  // ex 3.6
  def init[A](l: List[A]): List[A] = l match {
    case Cons(x, Cons(y, Nil)) => Cons(x, Nil)
    case Cons(x, xs) => Cons(x, init(xs))
  }

  def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
    case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    case Nil => z
  }

  // ex 3.9
  def length[A](as: List[A]): Int = foldRight(as, 0)((x, y) => 1 + y)

  // ex 3.10
  @tailrec
  def foldLeft[A, B](as: List[A], z: B)(f: (B, A) => B): B =
    as match {
      case Cons(x, xs) => foldLeft(xs, f(z, x))(f)
      case Nil => z
    }

  // ex 3.11
  def sum2(as: List[Int]): Int = foldLeft(as, 0)(_ + _)
  def product2(as: List[Double]): Double = foldLeft(as, 1.0)(_ * _)
  def length2[A](as: List[A]): Int = foldLeft(as, 0)((x, y) => x + 1)

  // ex 3.12
  def reverse[A](as: List[A]): List[A] = foldLeft(as, Nil: List[A])((x, y) => Cons(y, x))

  // ex 3.13
  def foldRight2[A, B](as: List[A], z: B)(f: (A, B) => B): B = foldLeft(reverse(as), z)((x, y) => f(y, x))

  // ex 3.14
//  def append[A](as: List[A], bs: List[A]): List[A] = foldRight(as, bs)(Cons(_, _))
  def append[A](as: List[A], bs: List[A]): List[A] = foldLeft(reverse(as), bs)((x, y) => Cons(y, x))

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
    println(drop(List(1, 2, 3, 4, 5, 6), 2))
    println(dropWhile(List(1, 2, 3, 4, 5, 6), (x: Int) => x > 3))
    println(dropWhile2(List(1, 2, 3, 4, 5, 6))(x => x > 3))
    println(init(List(1, 2, 3, 4, 5, 6)))

    println(foldRight(List(1, 2, 3, 4, 5, 6), 0)(_ + _))

    // ex 3.8
    println(foldRight(List(1, 2, 3, 4, 5, 6), Nil: List[Int])(Cons(_, _)))

    println(length(List(1, 2, 3)))

    println(foldLeft(List(1, 2, 3, 4, 5, 6), 0)(_ + _))

    // foldLeft 是逆序的
    println(foldLeft(List(1, 2, 3, 4, 5, 6), Nil: List[Int])((x, y) => Cons(y, x)))

    println(sum2(List(1, 2, 3)))
    println(product2(List(1, 2, 3)))
    println(length2(List(1, 2, 3)))

    println(reverse(List(1, 2, 3)))

    println(foldRight2(List(1, 2, 3, 4, 5, 6), Nil: List[Int])(Cons(_, _)))

    println(append(List(1, 2), List(3, 4)))
  }
}
