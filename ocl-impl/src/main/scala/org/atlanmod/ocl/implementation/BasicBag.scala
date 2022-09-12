package org.atlanmod.ocl.implementation

import org.atlanmod.ocl.{Bag, Collection, OclAny}

import scala.collection.mutable.ArrayBuffer

class BasicBag[E <: OclAny](value: Seq[E]) extends AbstractCollection[E](value) with Bag[E] {

  override def including(o: E): Bag[E] = new BasicBag(this :+ o)

  override def excluding(o: E): Bag[E] = {
    new BasicBag(this.filter(_ <> o))
  }

  override def flatten(): Bag[E] = ???

  override def reject(expression: E => Boolean): Collection[E] = {
    select((x: E) => !expression(x))
  }

  override def select(expression: E => Boolean): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      if (expression(f)) arrayBuffer += f
    })
    new BasicBag(arrayBuffer)
  }

  override def collect(expression: E => E): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      arrayBuffer += expression(f)
    })
    new BasicBag(arrayBuffer)
  }

  override def forAll(expression: E => Boolean): Boolean = {
    foreach(f => {
      if (!expression(f)) return false
    })
    true
  }
}
