package fpinscala.exercises.ch5.laziness

import Stream._

import scala.annotation.tailrec

trait Stream[+A] {
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h())
  }

  def take(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 1 => cons(h(), t().take(n-1))
    case Cons(h, _) if n == 1 => cons(h(), Empty)
    case Empty => empty
  }

  @tailrec
  final def drop(n: Int): Stream[A] = this match {
    case Cons(_, t) if n > 0 => t().drop(n-1)
    case _ => this
  }

  def takeWhile(p: A => Boolean): Stream[A] = this match {
    case Cons(h, t) if p(h()) => cons(h(), t().takeWhile(p))
    case _ => empty
  }

  def toList: List[A] = {
    @tailrec
    def go(s: Stream[A], acc: List[A]): List[A] = s match {
      case Empty => acc
      case Cons(h, t) => go(t(), h() :: acc)
    }

    go(this, Nil).reverse
  }

  def toListRecursive: List[A] = this match {
    case Empty => Nil
    case Cons(h, t) => h() :: t().toListRecursive
  }

  def exists(p: A => Boolean): Boolean = this match {
    case Cons(h, t) => p(h()) || t().exists(p)  // lazy
    case _ => false
  }

  def foldRight[B](z: => B)(f: (A, B) => B): B = this match {
    case Cons(h, t) => f(h(), t().foldRight(z)(f))
    case _ => z
  }

  def forAllNormal(p: A => Boolean): Boolean = this match {
    case Cons(h, t) => p(h()) && t().forAllNormal(p)
    case _ => true
  }

  def forAll(p: A => Boolean): Boolean =
    foldRight(true)((x, z) => z && p(x))

  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] =
    foldRight[Stream[A]](empty)((x, z) => if (p(x)) cons(x, z) else empty)

  def headOptionViaFoldRight: Option[A] =
    foldRight[Option[A]](None)((x, _) => Some(x))

  def map[B](f: A => B): Stream[B] =
    foldRight[Stream[B]](empty)((x, z) => cons(f(x), z))

  def filter(p: A => Boolean): Stream[A] =
    foldRight[Stream[A]](empty)((x, z) => if (p(x)) cons(x, z) else z)

  def flatMap[B](f: A => Stream[B]): Stream[B] =
    foldRight[Stream[B]](empty)((x, z) => f(x).append(z))

  def append[B >: A](b: => Stream[B]): Stream[B] =
    foldRight[Stream[B]](b)((x, z) => cons(x, z))

  def mapViaUnfold[B](f: A => B): Stream[B] =
    unfold(this)(x => x match {
      case Cons(h, t) => Some(f(h()), t())
      case _ => None
    })

  def takeViaUnfold(n: Int): Stream[A] =
    unfold((this, 0)) {
      case (Cons(h, t), i) if i < n => Some(h(), (t(), i + 1))
      case _ => None
    }

  def takeWhileViaUnfold(p: A => Boolean): Stream[A] =
    unfold(this){
      case Cons(h, t) if p(h()) => Some(h(), t())
      case _ => None
    }

  def zipWith[B, C](b: Stream[B])(f: (A, B) => C): Stream[C] =
    unfold((this, b)) {
      case (Cons(ha, ta), Cons(hb, tb)) => Some(f(ha(), hb()), (ta(), tb()))
      case _ => None
    }

  def zipAll[B](b: Stream[B]): Stream[(Option[A], Option[B])] =
    unfold((this, b)) {
      case (Cons(ha, ta), Cons(hb, tb)) => Some((Some(ha()), Some(hb())), (ta(), tb()))
      case (Cons(ha, ta), Empty) => Some((Some(ha()), None), (ta(), empty))
      case (Empty, Cons(hb, tb)) => Some((None, Some(hb())), (empty, tb()))
      case _ => None
    }

  def startsWith[B >: A](s: Stream[B]): Boolean =
    zipAll(s).takeWhile(!_._2.isEmpty).forAll {
      case (a, b) => a == b
    }

  def startsWith_1[B >: A](s: Stream[B]): Boolean =
    zipAll(s).takeWhile {
      case (_, Some(_)) => true
      case _ => false
    }.forAll {
      case (Some(a), Some(b)) if a == b => true
      case _ => false
    }

  def tails: Stream[Stream[A]] =
    unfold(this) {
      case Cons(h, t) => Some((Cons(h, t), t()))
      case _ => None
    } append Stream(empty)

  def hasSequence[B >: A](s: Stream[B]): Boolean =
    tails exists(_ startsWith s)

  // 这个最难
  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] =
    foldRight((z, Stream(z)))((a, p0) => {
      lazy val p1 = p0
      val b2 = f(a, p1._1)
      (b2, cons(b2, p1._2))
    })._2
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = cons(1, ones)

  def constant[A](a: A): Stream[A] =
    cons(a, constant(a))

  def from(n: Int): Stream[Int] =
    cons(n, from(n + 1))

  val fibs = {
    def go(p: Int, q: Int): Stream[Int] =
      cons(p, go(q, p+q))

    go(0, 1)
  }

  def fibs_1(n: Int): Stream[Int] = {
    @tailrec
    def go(p: Int, q: Int, i: Int, s: Stream[Int]): Stream[Int] =
      if (i < n) go(q, p+q, i+1, s.append(Stream(p)))
      else s

    go(0, 1, 0, empty)
  }

  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
    f(z) match {
      // case Some(x) => cons(x._1, unfold(x._2)(f))
      case Some((a, s)) => cons(a, unfold(s)(f))
      case None => empty
    }

  val fibsViaUnfold = unfold((0, 1))(x => Some(x._1, (x._2, x._1 + x._2)))

  def fromViaUnfold(n: Int): Stream[Int] = unfold(n)(x => Some(x, x+1))

  def constantViaUnfold(a: Int): Stream[Int] = unfold(a)(x => Some(x, x))

  val onesViaUnfold: Stream[Int] = unfold(1)(x => Some(x, x))
}
