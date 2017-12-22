//package edu.neu.coe.csye._7200.random

import scala.collection.immutable.Stream.cons
import scala.util.Random
/**
  * Monadic trait which defines a random-state.
  *
  * Created by scalaprof on 9/24/16.
  *
  * @tparam T the underlying type of this random state, i.e. the type of the result of calling get
  */
trait RandomState[T] {
  /**
    * @return the next random state in the pseudo-random series
    */
  def next: RandomState[T]

  /**
    * @return the value of this random state
    */
  def get: T

  /**
    * Method to map this random state into another random state
    * @param f the function to map a T value into a U value
    * @tparam U the underlying type of the resulting random state
    * @return a new random state
    */
  def map[U](f: T=>U): RandomState[U]

  /**
    * Method to flatMap this random state into another random state
    * @param f the function to map a T value into a RandomState[U] value
    * @tparam U the underlying type of the resulting random state
    * @return a new random state
    */
  def flatMap[U](f: T=>RandomState[U]): RandomState[U] = f.apply(get)         // TODO 10 points

  /**
    * @return a stream of T values
    */
  def toStream: Stream[T] =  cons(get,next.toStream)                // TODO 12 points
}

/**
  * A concrete implementation of RandomState based on the Java random number generator
  * @param n the random Long that characterizes this random state
  * @param g the function which maps a Long value into a T
  * @tparam T the underlying type of this random state, i.e. the type of the result of calling get
  */
case class JavaRandomState[T](n: Long, g: Long=>T) extends RandomState[T] {
  def next: RandomState[T] = JavaRandomState[T](new Random(n).nextLong(), g)    // TODO 7 points
  def get: T = g.apply(n) // TODO 5 points
  def map[U](f: (T) => U): RandomState[U] = JavaRandomState[U](n, g.andThen(f)) // TODO 13 points
}

object RandomState {
  def apply(n: Long): RandomState[Long] = JavaRandomState[Long](n,identity).next
  def apply(): RandomState[Long] = apply(System.currentTimeMillis)
  val longToDouble: Long=>Double = _.toDouble/Long.MaxValue                   // TODO 4 points
  val doubleToUniformDouble: Double=>UniformDouble = {x => UniformDouble((x+1)/2)}
}

/**
  * This is essentially a wrapper of Double where (implicitly) 0 <= x <= 1.
  * Note that we would like to specify it as a Value type but require statements are not legal in Value types
  */
case class UniformDouble(x: Double) {
  require(x>=0.0 && x<=1.0)
  def +(y: Double) = x + y
}

object UniformDoubleRandomState