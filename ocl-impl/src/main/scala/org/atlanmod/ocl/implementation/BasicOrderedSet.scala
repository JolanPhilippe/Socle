package org.atlanmod.ocl.implementation

import org.atlanmod.ocl.{Collection, OclAny, OrderedSet}

import scala.collection.mutable.ArrayBuffer

class BasicOrderedSet[E <: OclAny](value: Seq[E]) extends AbstractCollection[E](value) with OrderedSet[E] {

  override def prepend(o: E): OrderedSet[E] = new BasicOrderedSet(if (!this.includes(o)) o +: this else this)

  override def insertAt(n: Integer, o: E): OrderedSet[E] = {
    new BasicOrderedSet((this.subOrderedSet(0, n - 1) :+ o) ++ this.subOrderedSet(n, value.length))
  }

  override def subOrderedSet(lower: Integer, upper: Integer): OrderedSet[E] =
    new BasicOrderedSet(value.slice(lower, upper + 1))

  override def indexOf(o: E): Integer = this.indexWhere(p => p == o)

  override def first(): E = {
    if (value == null)
      null
    value(0)
  }

  override def union(c: Collection[E]): OrderedSet[E] = {
    var unionWithDuplicates = new BasicOrderedSet(this ++ c)
    var set = new BasicOrderedSet(Seq.empty[E])
    var i = 0
    for (i <- 0 until unionWithDuplicates.length) {
      set = new BasicOrderedSet(set including unionWithDuplicates.at(i))
    }
    set
  }

  override def at(n: Integer): E = value(n)

  override def including(o: E): OrderedSet[E] = new BasicOrderedSet(if (!this.includes(o)) this.append(o) else this)

  override def append(o: E): OrderedSet[E] = new BasicOrderedSet(if (!this.includes(o)) this :+ o else this)

  // useful after collectNested() has been called
  override def flatten(): OrderedSet[E] = ???

  override def excluding(o: E): OrderedSet[E] = new BasicOrderedSet(this.filter(_ <> o))

  override def reject(expression: E => Boolean): Collection[E] = {
    select((x: E) => !expression(x))
  }

  override def select(expression: E => Boolean): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      if (expression(f)) arrayBuffer += f
    })
    new BasicOrderedSet(arrayBuffer)
  }

  override def collect(expression: E => E): Collection[E] = {
    var arrayBuffer = ArrayBuffer[E]()
    value.foreach(f => {
      arrayBuffer += expression(f)
    })
    new BasicOrderedSet(arrayBuffer)
  }

  override def forAll(expression: E => Boolean): Boolean = {
    foreach(f => {
      if (!expression(f)) return false
    })
    true
  }
}
