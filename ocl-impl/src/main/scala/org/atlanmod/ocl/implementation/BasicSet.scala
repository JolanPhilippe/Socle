package org.atlanmod.ocl.implementation

import org.atlanmod.ocl.{Collection, OclAny, Set}

import scala.collection.mutable.ArrayBuffer


class BasicSet[E <: OclAny](value: Seq[E]) extends AbstractCollection[E](value) with Set[E] {

  override def union(c: Collection[E]): Set[E] = {
    var set = new BasicSet(Seq.empty[E])
    (this ++ c).foreach(element => set = new BasicSet[E](set including element))
    set
  }

  override def intersection(c: Collection[E]): Set[E] = {
    var set = new BasicSet(Seq.empty[E])
    c.foreach(element => {
      if (this.includes(element)) set = new BasicSet[E](set including element)
    })
    set
  }

  override def -(s: Set[E]): Set[E] = {
    var set = new BasicSet(Seq.empty[E])
    this.foreach(element => {
      if (!s.includes(element)) set = new BasicSet[E](set including element)
    })
    set
  }

  override def including(o: E): Set[E] = {
    new BasicSet(if (!this.includes(o)) this :+ o else this)
  }

  override def excluding(o: E): Set[E] = {
    new BasicSet(this.filter(_ <> o))
  }

  override def symmetricDifference(s: Set[E]): Set[E] = {
    var set = new BasicSet(Seq.empty[E])
    (this ++ s).foreach(element => {
      if (!(this.includes(element) && s.includes(element))) set = new BasicSet[E](set including element)
    })
    set
  }

  override def reject(expression: E => Boolean): Collection[E] = {
    select((x: E) => !expression(x))
  }

  override def select(expression: E => Boolean): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      if (expression(f)) arrayBuffer += f
    })
    new BasicSet(arrayBuffer)
  }

  override def collect(expression: E => E): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      arrayBuffer += expression(f)
    })
    new BasicSet(arrayBuffer)
  }

  override def forAll(expression: E => Boolean): Boolean = {
    foreach(f => {
      if (!expression(f)) return false
    })
    true
  }
}
