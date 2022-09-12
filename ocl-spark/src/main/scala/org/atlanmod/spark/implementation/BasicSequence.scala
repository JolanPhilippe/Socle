package org.atlanmod.spark.implementation

import org.apache.spark.rdd.RDD
import org.atlanmod.ocl.{Bag, Collection, OclAny, OclType, Sequence, Set}

import scala.reflect.ClassTag

/**
  * TODO: can we still keep the Sequence order when data are parallelized between RDDs?
  *
  * @param value
  * @tparam E
  */
class BasicSequence[E <: OclAny](value : RDD[E]) extends AbstractCollection[E](value) with Sequence[E] with InitSpark {

  override def union(c: Collection[E]): Sequence[E] = {
    val basicType = c.iterator.next().getClass
    // the c collection parameter might already be parallelized if it is comes from the Spark package (because of RDDs)
    val cRDD = sc.parallelize(c)(ClassTag.apply(basicType))
    val union = value.union(cRDD)
    new BasicSequence(union)
  }

  override def reject(expression: E => Boolean): Collection[E] = {
    select((x: E) => !expression(x))
  }

  override def select(expression: E => Boolean): Collection[E] = {
    new BasicSequence(value.filter(f => expression(f)))
  }

  override def collect(expression: E => E): Collection[E] = {
    // keeping the stored type as a class member from the creation might be more efficient
    val basicType = value.toLocalIterator.next().getClass
    new BasicSequence(value.map(f => expression(f))(ClassTag.apply(basicType)))
  }

  override def forAll(expression: E => Boolean): Boolean = {
    value.map(f => expression(f)).reduce((a,b) => a && b)
  }

  // useful after collectNested() has been called
  override def flatten(): Sequence[E] = ???

  override def prepend(c: E): Sequence[E] = ???

  override def insertAt(n: Integer, o: E): Sequence[E] = ???

  override def subSequence(lower: Integer, upper: Integer): Sequence[E] = ???

  // TODO: think of an efficient implementation
  override def at(n: Integer): E = {
    super.apply(n)
  }

  override def indexOf(o: E): Integer = ???

  override def first(): E = ???

  override def including(o: E): Sequence[E] = ???

  override def append(o: E): Sequence[E] = ???

  override def excluding(o: E): Sequence[E] = {
    new BasicSequence(value.filter(_ <> o))
  }

}
