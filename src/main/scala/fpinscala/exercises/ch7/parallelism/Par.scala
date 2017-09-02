package fpinscala.exercises.ch7.parallelism

case class Par[A](a: A)

object Par {
  def unit[A](a: A): Par[A] = Par(a)
}