package fpinscala.exercises.ch5.laziness

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
  }
}
