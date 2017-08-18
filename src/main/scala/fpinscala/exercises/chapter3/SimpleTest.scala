package fpinscala.exercises.chapter3

import fpinscala.exercises.chapter3.List._

object SimpleTest {
  def main(args: Array[String]): Unit = {
    // list
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

    println(foldRightViaFoldLeft(List(1, 2, 3, 4, 5, 6), Nil: List[Int])(Cons(_, _)))

    println(append(List(1, 2), List(3, 4)))

    println(append_all(List(1, 2), List(3), List(4, 5)))

    println(addOne(List(1, 2, 3)))

    println(List.toString(List(1.0, 2.0, 3.0)))

    println(map(List(1, 2, 3))(x => x + 5))

    println(filter(List(1, 2, 3))(x => x > 2))

    println(flatMap(List(1, 2, 3))(x => List(s"${x} is string")))

    println(filter2(List(1, 2, 3))(x => x > 2))

    println(addPairwise(List(1, 2, 3), List(4, 5, 6)))

    println(zipWith(List(1, 2, 3), List(4, 5, 6))((x, y) => x + y))

    assert(hasSubsequence(List(1, 2, 3, 4), List(1, 2)))
    assert(hasSubsequence(List(1, 2, 3, 4), List(2, 3)))
    assert(hasSubsequence(List(1, 2, 3, 4), List(3, 4)))
    assert(hasSubsequence(List(1, 2, 3, 3, 4), List(3, 4)))
    assert(!hasSubsequence(List(1, 2, 3, 4), List(2, 4)))

    // tree
    val t = Branch(Branch(Branch(Leaf(1), Leaf(2)), Leaf(2)), Leaf(3))
    println(Tree.size(t))

    println(Tree.map(t)(x => x + 1))
    assert(Tree.maximum(t) == 3)
    assert(Tree.depth(t) == 4)
  }
}
