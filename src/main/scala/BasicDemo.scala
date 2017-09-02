import scala.annotation.tailrec

class MyString(s: String) {
  def apply(i: Int): Char = s(i)
}

object MyString {
  def apply(s: String) = new MyString(s)
}

trait Logged {
  def log(msg: String) { }
}

class Hello extends Logged {
  def hello(name: String): Unit = {
    log(s"Hello, $name")
  }
}

trait ConsoleLogger extends Logged {
  override def log(msg: String): Unit = { println(msg) }
}

trait TimestampLogger extends Logged {
  override def log(msg: String): Unit = super.log(new java.util.Date() + " " + msg)
}

trait ShortLogger extends Logged {
  val maxLength = 15

  override def log(msg: String): Unit =
    super.log(if (msg.length <= maxLength) msg else msg.substring(0, maxLength - 3) + "...")
}

object BasicDemo {

  // 变长参数
  def sum(ints: Int*): Int = {
    @tailrec
    def go(s: Seq[Int], acc: Int): Int = {
      if (s.isEmpty) acc
      else go(s.tail, acc + s.head)
    }

    go(ints, 0)
  }

  def main(args: Array[String]): Unit = {
    val s = MyString("sss")
    println(s(0))

    println(sum(1, 2, 3, 4))

    for (i <- 0 to 10) println(i)

    // trait
    val h = new Hello with ConsoleLogger
    h.hello("World")

    // with 的顺序很关键
    val acct1 = new Hello with ConsoleLogger with TimestampLogger with ShortLogger
    acct1.log("通过编写基于类的动作，我们可以将应用组织为模块的风格。例如， 如下目录结构可用于组织控制器相关代码")
    val acct2 = new Hello with ConsoleLogger with ShortLogger with TimestampLogger
    acct2.log("通过编写基于类的动作，我们可以将应用组织为模块的风格。例如， 如下目录结构可用于组织控制器相关代码")
  }
}
