package org.atlanmod.ocl.implementation

import org.atlanmod.ocl
import org.atlanmod.ocl._

abstract class AbstractCollection[E <: OclAny](var value: Seq[E]) extends Collection[E] {

  override def notEmpty(): Boolean = !value.isEmpty

  override def asBag(): Bag[OclAny] = new BasicBag(value)

  override def asSequence(): Sequence[OclAny] = new BasicSequence(value)

  override def asSet(): ocl.Set[OclAny] = new BasicSet(value)

  override def length: Int = value.length

  override def +(other: OclAny): OclAny = ???

  override def <>(other: OclAny): Boolean = !(other == this)

  override def oclIsUndefined(): Boolean = this == null

  override def ==(other: OclAny): Boolean = other == this

  override def oclIsKindOf(t: OclType): Boolean = ???

  override def oclIsTypeOf(t: OclType): Boolean = t.getClass() == this.getClass()

  override def oclType(): OclType = ???

  override def refGetValue(s: String): OclAny = ???

  override def refSetValue(name: String, v: OclAny): Unit = ???

  override def refImmediateComposite(): Unit = ???

  override def refInvokeOperation(opName: String, args: Sequence[OclAny]): Unit = ???

  override def iterator: Iterator[E] = return value.iterator

  override def count(o: E): Int = value.length

  override def includesAll(c: Collection[E]): Boolean = {
    for (v <- c)
      if (!this.includes(v)) return false // keep the return keyword
    true
  }

  override def excludesAll(c: Collection[E]): Boolean = {
    for (v <- c)
      if (!this.excludes(v)) return false // keep the return keyword
    true
  }

  override def excludes(o: E): Boolean = !includes(o)

  override def includes(o: E): Boolean = {
    value.foreach(v => {
      if (v == o) return true
    }) // keep the return keyword
    false
  }

  override def sum: E =
    value.reduce((a, b) => (a + b).asInstanceOf[E])

  override def apply(idx: Int): E = value(idx)
}