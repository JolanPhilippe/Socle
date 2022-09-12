package org.atlanmod.spark.implementation

import org.apache.spark.rdd.RDD
import org.atlanmod.ocl.{Collection, OclAny}

import scala.reflect.ClassTag

object BasicSequenceTest extends App with InitSpark {

  override def main(args: Array[String]) = {
    testUnion()
    testSelect()
    testReject()
    testCollect()
    testForAll()
  }

  private def testImplicitClassTag[E <: OclAny](c: Collection[E], d: Collection[E])(implicit m: ClassTag[E]) : RDD[E] ={
    println(m)
    var a = sc.parallelize(c)
    var b = sc.parallelize(d)
    a.union(b)
  }

  private def testExplicitClassTag[E <: OclAny](c: Collection[E], d: Collection[E]) : RDD[E] ={
    val basicInt = new BasicInteger(0)
    var a = sc.parallelize(c)(ClassTag.apply(basicInt.getClass))
    var b = sc.parallelize(d)(ClassTag.apply(basicInt.getClass))
    a.union(b)
  }

  def testUnion(): Unit = {
    val basicInt = new BasicInteger(0)
    val seqA = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(4))
    val seqB = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(3))
    val seqAUnionB = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3), new BasicInteger(4))
    val basicSetA = new BasicSequence(sc.parallelize(seqA)(ClassTag.apply(basicInt.getClass)))
    // could also be any Collection[E] without need for parallelize
    val basicSetB = new BasicSequence(sc.parallelize(seqB)(ClassTag.apply(basicInt.getClass)))
    val union = basicSetA.union(basicSetB)
    assert(seqAUnionB.length == union.length)

    val basicString = new BasicString("")
    val seqC = Vector[BasicString](new BasicString("A"), new BasicString("A"))
    val seqD = Vector[BasicString](new BasicString("A"), new BasicString("A"))
    val seqCUnionD = Vector[BasicString](new BasicString("A"), new BasicString("A"), new BasicString("A"), new BasicString("A"))
    val basicSetC = new BasicSequence(sc.parallelize(seqC)(ClassTag.apply(basicString.getClass)))
    // could also be any Collection[E] without need for parallelize
    val basicSetD = new BasicSequence(sc.parallelize(seqD)(ClassTag.apply(basicString.getClass)))
    val unionB = basicSetC.union(basicSetD)
    assert(seqCUnionD.length == unionB.length)
  }

  def testSelect(): Unit = {
    val basicInt = new BasicInteger(0)
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeqA = new BasicSequence(sc.parallelize(a)(ClassTag.apply(basicInt.getClass)))
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var selectedSet = basicSeqA.select(isEvenFun)
    assert(selectedSet.length == 1)
  }

  def testReject(): Unit = {
    val basicInt = new BasicInteger(0)
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeqA = new BasicSequence(sc.parallelize(a)(ClassTag.apply(basicInt.getClass)))
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var rejectedSet = basicSeqA.reject(isEvenFun)
    assert(rejectedSet.length == 2)
  }

  def testCollect(): Unit = {
    val basicInt = new BasicInteger(0)
    val a = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(1), new BasicInteger(2))
    val basicSeqA = new BasicSequence(sc.parallelize(a)(ClassTag.apply(basicInt.getClass)))
    var squareFun = (a: BasicInteger) => (new BasicInteger((a.value * a.value)))
    var squaredSeq = basicSeqA.collect(squareFun)
    var i = 0
    for (i <- 0 to basicSeqA.length - 1) {
      assert(basicSeqA.at(i).value * basicSeqA.at(i).value == squaredSeq(i).value)
    }
  }

  def testForAll(): Unit = {
    val basicInt = new BasicInteger(0)
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(1), new BasicInteger(3), new BasicInteger(1))
    val basicSeq = new BasicSequence(sc.parallelize(seq)(ClassTag.apply(basicInt.getClass)))
    val res = basicSeq.forAll(s => s > new BasicInteger(Int.MaxValue))
    assert(res == false)

    val resB = basicSeq.forAll(s => s == new BasicInteger(1))
    assert(resB == false)

    val resC = basicSeq.forAll(s => s != new BasicInteger(9))
    assert(resC == true)
  }

}
