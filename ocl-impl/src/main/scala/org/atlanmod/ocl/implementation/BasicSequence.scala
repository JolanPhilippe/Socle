package org.atlanmod.ocl.implementation

import org.atlanmod.ocl.{Collection, OclAny, Sequence}

import scala.collection.mutable.ArrayBuffer

class BasicSequence[E <: OclAny](value: Seq[E]) extends AbstractCollection[E](value) with Sequence[E] {

  override def reject(expression: E => Boolean): Collection[E] = {
    select((x: E) => !expression(x))
  }

  override def select(expression: E => Boolean): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      if (expression(f)) arrayBuffer += f
    })
    new BasicSequence(arrayBuffer)
  }

  override def collect(expression: E => E): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      arrayBuffer += expression(f)
    })
    new BasicSequence(arrayBuffer)
  }

  override def forAll(expression: E => Boolean): Boolean = {
    foreach(f => {
      if (!expression(f)) return false
    })
    true
  }

  override def union(c: Collection[E]): Sequence[E] = {
    new BasicSequence(c.union(this))
  }

  // useful after collectNested() has been called
  override def flatten(): Sequence[E] = ???

  override def prepend(c: E): Sequence[E] = new BasicSequence(c +: this)

  override def insertAt(n: Integer, o: E): Sequence[E] = {
    new BasicSequence((this.subSequence(0, n - 1) :+ o) ++ this.subSequence(n, value.length))
  }

  override def subSequence(lower: Integer, upper: Integer): Sequence[E] =
    new BasicSequence(value.slice(lower, upper + 1))

  override def at(n: Integer): E = value(n)

  override def indexOf(o: E): Integer = this.indexWhere(p => p == o)

  override def first(): E = {
    if (value == null)
      null
    value(0)
  }

  override def including(o: E): Sequence[E] = this.append(o)

  override def append(o: E): Sequence[E] = new BasicSequence(this :+ o)

  override def excluding(o: E): Sequence[E] = {
    new BasicSequence(this.filter(_ <> o))
  }
}
