package fpinscala.exercises.chapter4

sealed trait Either[+E, +A] {
  def map[B](f: A => B): Either[E, B] = this match {
    case Right(a) => Right(f(a))
    case Left(e) => Left(e)
  }

  def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
    case Right(a) => Right(a)
    case Left(_) => b
  }

  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
    case Right(a) => f(a)
    case Left(e) => Left(e)
  }

  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] =
    for {
      aa <- this
      bb <- b
    } yield f(aa, bb)
//    this.flatMap(aa => b.map(bb => f(aa, bb))) // for 是这种语法的语法糖

  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] =
    es.foldRight[Either[E, List[A]]](Right(Nil))((x, y) => x.map2(y)(_ :: _))

  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    as.foldRight[Either[E, List[B]]](Right(Nil))((x, y) =>
      for {
        xx <- f(x)
        yy <- y
      } yield xx :: yy)

  def sequenceViaTraverse[E, A](es: List[Either[E, A]]): Either[E, List[A]] =
    traverse(es)(x => x)

}

case class Left[+E](value: E) extends Either[E, Nothing] // 失败
case class Right[+A](value: A) extends Either[Nothing, A] // right 成功
