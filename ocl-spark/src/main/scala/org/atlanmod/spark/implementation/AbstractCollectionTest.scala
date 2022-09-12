package org.atlanmod.spark.implementation

import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

object AbstractCollectionTest extends App with InitSpark {

  override def main(args: Array[String]) = {
    testIncludes()
    testExcludes()
    testIncludesAll()
    testExcludesAll()
    testLength()
    testAt()
    testSum()
  }


  def testIncludes(): Unit = {
    val seqA = Vector[BasicInteger](new BasicInteger(5), new BasicInteger(42), new BasicInteger(4))
    val basicInt = new BasicInteger(0)
    val seqARDD = sc.parallelize(seqA)(ClassTag.apply(basicInt.getClass))
    val set = new BasicSequence(seqARDD)
    assert(set.includes(new BasicInteger(42)) == true)
    assert(set.includes(new BasicInteger(24)) == false)
  }

  def testExcludes(): Unit = {
    val seqA = Vector[BasicInteger](new BasicInteger(5), new BasicInteger(42), new BasicInteger(4))
    val basicInt = new BasicInteger(0)
    val seqARDD = sc.parallelize(seqA)(ClassTag.apply(basicInt.getClass))
    val set = new BasicSequence(seqARDD)
    assert(set.excludes(new BasicInteger(42)) == false)
    assert(set.excludes(new BasicInteger(24)) == true)
  }

  def testIncludesAll(): Unit = {
    val basicInt = new BasicInteger(0)

    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicSequence(sc.parallelize(seq)(ClassTag.apply(basicInt.getClass)))

    val seqB = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(3))
    val basicSetB = new BasicSequence(sc.parallelize(seqB)(ClassTag.apply(basicInt.getClass)))
    assert(basicSetB.includesAll(basicSet) == false)
    assert(basicSet.includesAll(basicSetB) == true)

    val seqC = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2),
      new BasicInteger(3), new BasicInteger(4))
    val basicSetC = new BasicSequence(sc.parallelize(seqC)(ClassTag.apply(basicInt.getClass)))
    assert(basicSetC.includesAll(basicSet) == true)
    assert(basicSet.includesAll(basicSetC) == false)
  }

  def testExcludesAll(): Unit = {
    val basicInt = new BasicInteger(0)

    val a = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(1))
    val b = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(7))
    val basicSetA = new BasicSequence(sc.parallelize(a)(ClassTag.apply(basicInt.getClass)))
    val basicSetB = new BasicSequence(sc.parallelize(b)(ClassTag.apply(basicInt.getClass)))
    assert(basicSetA.excludesAll(basicSetB) == false)
    assert(basicSetB.excludesAll(basicSetA) == false)

    val c = Vector[BasicInteger](new BasicInteger(9))
    val basicSetC = new BasicSequence(sc.parallelize(c)(ClassTag.apply(basicInt.getClass)))
    assert(basicSetA.excludesAll(basicSetC) == true)
    assert(basicSetC.excludesAll(basicSetA) == true)
  }

  def testSum(): Unit = {
    val basicInt = new BasicInteger(0)
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(1), new BasicInteger(2))
    val basicSet = new BasicSequence(sc.parallelize(seq)(ClassTag.apply(basicInt.getClass)))
    assert(basicSet.sum().value == 4)

    val basicString = new BasicString("")
    val seqB = Vector[BasicString](new BasicString("Hubert Bonisseur"), new BasicString(" de La Bath"))
    val basicSetB = new BasicSequence(sc.parallelize(seqB)(ClassTag.apply(basicString.getClass)))
    // TODO: check if this is the desired behaviour
    // print(basicSetB.sum().value)
    assert(basicSetB.sum().value == "Hubert Bonisseur de La Bath" ||  basicSetB.sum().value == " de La BathHubert Bonisseur")
  }

  def testLength(): Unit = {
    val basicInt = new BasicInteger(0)
    val a = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(1))
    val basicSetA = new BasicSequence(sc.parallelize(a)(ClassTag.apply(basicInt.getClass)))
    assert(basicSetA.length == 2)
  }

  def testAt(): Unit = {
    val seqA = Vector[BasicInteger](new BasicInteger(0), new BasicInteger(1), new BasicInteger(2))
    val basicInt = new BasicInteger(0)
    val set = new BasicSequence(sc.parallelize(seqA)(ClassTag.apply(basicInt.getClass)))
    assert(set.at(0).value == 0)
    assert(set.at(1).value == 1)
    assert(set.at(2).value == 2)
  }

}
