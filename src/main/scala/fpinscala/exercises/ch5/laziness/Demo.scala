package fpinscala.exercises.ch5.laziness

import Stream._

object Demo {

  def maybeTwice(b: Boolean, i: => Int) = if (b) i + i else 0
  def maybeTwice2(b: Boolean, i: => Int) = {
    lazy val j = i  // 延迟对lazy变量求值，第一次引用时被求值，不会触发重复求值
    if (b) j + j else 0
  }
  def normal_func(b: Boolean, i: Int) = if (b) i + i else 0

  def main(args: Array[String]): Unit = {
    // 表达式包装为 thunk，会在方法体中引用的地方求值一次，引用多次就会求值多次
    assert(maybeTwice(true, { println("lazy"); 1 + 41}) == 84)

    assert(maybeTwice(true, { println("lazy"); 1 + 41}) == 84)
    assert(normal_func(true, { println("normal"); 1 + 41}) == 84)

    println(Stream(1, 2, 3))
    println(Stream(1, 2, 3).toList)
    println(Stream(1, 2, 3).take(2).toList)
    println(Stream(1, 2, 3).forAll(_ == 4))
    println(Stream(1, 2, 3).forAll(x => { print("."); true }))

    println(Stream(1, 2, 3).takeWhileViaFoldRight(_ < 3).toList)
    println(Stream(1, 2, 3).takeWhileViaFoldRight(_ == 2).toList)

    println(Stream(1, 2, 3).headOption)
    println(Stream.empty.headOption)
    println(Stream(1, 2, 3).headOptionViaFoldRight)
    println(Stream.empty.headOptionViaFoldRight)

    println(Stream(1, 2, 3, 2, 4, 5).filter(_ > 2).toList)

    println(Stream(1, 2, 3).map(_ + 1).flatMap(x => Stream(x, x)).toList)

    println(Stream(1, 2, 3).mapViaUnfold(_ + 1).append(Stream(4, 5, 6)).toList)

    println(ones.take(5).toList)
    println(onesViaUnfold.takeViaUnfold(5).toList)

    println(constant(4).take(10).toList)
    println(constantViaUnfold(4).take(10).toList)

    println(fibs.take(10).toList)
    println(fibsViaUnfold.take(10).toList)

    println(from(5).take(5).toList)
    println(fromViaUnfold(5).take(5).toList)

    println(Stream(1, 2, 3).takeWhileViaUnfold(_ < 2).toList)

    println(Stream(1, 2, 3).tails.map(_.toList).toList)

    assert(Stream(1, 2, 3).startsWith(Stream(1, 2, 3)))
    assert(Stream(1, 2, 3).startsWith(Stream(1, 2)))
    assert(Stream(1, 2, 3).startsWith(Stream(1)))
    assert(!Stream(1, 2, 3).startsWith(Stream(1, 2, 3, 4)))
    assert(!Stream(1, 2, 3).startsWith(Stream(2, 3)))

    println(Stream(1, 2, 3).scanRight(0)(_ + _).toList)
  }
}
