package fpinscala.exercises.chapter3

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  // ex 3.25
  def size[A](t: Tree[A]): Int = t match {
    case Leaf(v) => 1
    case Branch(l, r) => size(l) + size(r) + 1
  }

  // ex 3.26
  def maximum(t: Tree[Int]): Int = {
    def go(t: Tree[Int], m: Int): Int = t match {
      case Leaf(v) => Math.max(v, m)
      case Branch(l, r) => Math.max(go(l, m), go(r, m))
    }

    go(t, Int.MinValue)
  }

  // ex 3.27
  def depth[A](t: Tree[A]): Int = {
    def go(t: Tree[A], d: Int): Int = t match {
      case Leaf(_) => d + 1
      case Branch(l, r) => Math.max(go(l, d+1), go(r, d+1))
    }

    go(t, 0)
  }

  // ex 3.28
  def map[A, B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l, r) => Branch(map(l)(f), map(r)(f))
  }
}
