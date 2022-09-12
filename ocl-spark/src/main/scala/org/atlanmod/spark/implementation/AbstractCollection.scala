package org.atlanmod.spark.implementation

import org.apache.spark.rdd.RDD
import org.atlanmod.ocl.{Collection, OclAny, Bag, OclType, Sequence, Set}

import scala.reflect.ClassTag

abstract class AbstractCollection[E <: OclAny](var value : RDD[E]) extends Collection[E] with InitSpark {

  override def includes(o: E): Boolean = {
    value.map(f => o == f).reduce((a, b) => a || b)
  }

  override def excludes(o: E): Boolean = ! includes(o)

  // TODO: think about parallelizing c
  override def includesAll(c: Collection[E]): Boolean = {
    c.map(v => includes(v)).reduce((a, b) => a && b)
  }

  // TODO: think about parallelizing c
  override def excludesAll(c: Collection[E]): Boolean = {
    c.map(v => excludes(v)).reduce((a, b) => a && b)
  }

  override def sum(): E =
    value.reduce((a, b) => (a + b).asInstanceOf[E])

  override def length: Int = value.count().asInstanceOf[Int]

  override def notEmpty(): Boolean = value.isEmpty

  override def asBag(): Bag[OclAny] = ???

  override def asSequence(): Sequence[OclAny] = ???

  override def asSet(): Set[OclAny] = ???

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

  override def iterator: Iterator[E] = value.toLocalIterator

  override def count(o: E): Int = ???

  // TODO: think of an efficient implementation
  override def apply(idx: Int): E = {
    val basicType = value.toLocalIterator.next().getClass
    value.zipWithIndex.filter(_._2==idx).map(_._1)(ClassTag.apply(basicType)).first()
  }
}
