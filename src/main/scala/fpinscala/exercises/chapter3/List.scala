package fpinscala.exercises.chapter3

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
  def length[A](as: List[A]): Int = foldRight(as, 0)((_, acc) => 1 + acc)

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
  def foldRightViaFoldLeft[A, B](as: List[A], z: B)(f: (A, B) => B): B = foldLeft(reverse(as), z)((x, y) => f(y, x))

  // ex 3.14
//  def append[A](as: List[A], bs: List[A]): List[A] = foldRight(as, bs)(Cons(_, _))
  def append[A](as: List[A], bs: List[A]): List[A] = foldLeft(reverse(as), bs)((x, y) => Cons(y, x))

  // ex 3.15
  def append_all[A](as: List[A]*): List[A] =
    if (as.isEmpty) Nil
    else append(as.head, append_all(as.tail: _*))

  // ex 3.16
  def addOne(as: List[Int]): List[Int] = foldRight(as, Nil: List[Int])((x, y) => Cons(x, y))

  // ex 3.17
  def toString[A](as: List[A]): List[String] = foldRight(as, Nil: List[String])((x, y) => Cons(x.toString, y))

  // ex 3.18
  def map[A, B](as: List[A])(f: A => B): List[B] = foldRight(as, Nil: List[B])((x, y) => Cons(f(x), y))

  // ex 3.19
  def filter[A](as: List[A])(f: A => Boolean): List[A] = foldRight(as, Nil: List[A])((x, y) =>
    if (f(x)) Cons(x, y) else y )

  // ex 3.20
  def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = foldRight(as, Nil: List[B])((x, y) => append(f(x), y))

  // ex 3.21
  def filter2[A](as: List[A])(f: A => Boolean): List[A] = append_all(flatMap(as)(x => if (f(x)) Cons(x, Nil) else Nil))

  // ex 3.22
  def addPairwise(a: List[Int], b: List[Int]): List[Int] = (a, b) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(x, xs), Cons(y, ys)) => Cons(x+y, addPairwise(xs, ys))
  }

  // ex 3.23
  def zipWith[A, B, C](a: List[A], b: List[B])(f: (A, B) => C): List[C] = (a, b) match {
    case (Nil, _) => Nil
    case (_, Nil) => Nil
    case (Cons(x, xs), Cons(y, ys)) => Cons(f(x, y), zipWith(xs, ys)(f))
  }

  // ex 3.24
  @tailrec
  def startsWith[A](l: List[A], h: List[A]): Boolean = (l, h) match {
    case (_, Nil) => true
    case (Cons(x, xs), Cons(y, ys)) if x == y => startsWith(xs, ys)
    case _ => false
  }

  @tailrec
  def hasSubsequence[A](sup: List[A], sub: List[A]): Boolean = sup match {
    case Nil => Nil == sub
    case _ if startsWith(sup, sub) => true
    case Cons(x, xs) => hasSubsequence(xs, sub)
  }
}
