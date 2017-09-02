package fpinscala.exercises.ch8.testing

import fpinscala.exercises.ch6.state.{RNG, State}

//case class Gen[A](sample: State[RNG, A])
//
//object Gen {
//
//  def apply[A](sample: State[RNG, A]): Gen[A] = new Gen(sample)
//}

trait Prop {
  def check: Boolean
  def &&(p: Prop): Prop = new Prop {
    override def check: Boolean = this.check && p.check
  }
}
