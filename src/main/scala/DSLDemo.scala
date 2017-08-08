object DSLDemo {
  object Now

  object simulate {
    def once(behavior: () => Unit) = new {
      def right(now: Now.type) : Unit = behavior()
    }
  }

  def someAction(): Unit = println("Hello")

  def main(args: Array[String]): Unit = {
    simulate once {() => someAction()} right Now
  }
}
