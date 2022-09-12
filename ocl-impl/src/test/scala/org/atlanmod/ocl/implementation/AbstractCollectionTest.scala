package org.atlanmod.ocl.implementation

import org.scalatest.funsuite.AnyFunSuite

class AbstractCollectionTest extends AnyFunSuite {

  test("Test Includes") {
    val seq = Vector[BasicInteger](new BasicInteger(1))
    val basicSet = new BasicSequence(seq)
    assert(basicSet.includes(new BasicInteger(1)) == true)
    assert(basicSet.includes(new BasicInteger(12)) == false)
  }


  test("Excludes") {
    val seq = Vector[BasicInteger](new BasicInteger(1))
    val basicSet = new BasicSequence(seq)
    assert(basicSet.excludes(new BasicInteger(1)) == false)
    assert(basicSet.excludes(new BasicInteger(12)) == true)
  }

  test("IncludesAll") {
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicSequence(seq)
    assert(basicSet.includesAll(basicSet) == true)

    val seqB = Vector[BasicInteger](new BasicInteger(45646))
    val basicSetB = new BasicSequence(seqB)
    assert(basicSetB.includesAll(basicSet) == false)

    val seqC = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(45646))
    val basicSetC = new BasicSequence(seqC)
    assert(basicSetC.includesAll(basicSet) == false)

    val seqD = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(1))
    val basicSetD = new BasicSequence(seqD)
    assert(basicSet.includesAll(basicSetD) == true)
  }

  test("ExcludesAll") {
    val a = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(1), new BasicInteger(2))
    val b = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(7))
    val basicSetA = new BasicSequence(a)
    val basicSetB = new BasicSequence(b)
    assert(basicSetA.excludesAll(basicSetB) == false)

    val c = Vector[BasicInteger](new BasicInteger(9), new BasicInteger(7))
    val basicSetC = new BasicSequence(c)
    assert(basicSetA.excludesAll(basicSetC) == true)
  }

  test("Sum") {
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(1), new BasicInteger(2))
    val basicSet = new BasicSequence(seq)
    assert(basicSet.sum().value == 4)

    val seqB = Vector[BasicString](new BasicString("Hubert Bonisseur"), new BasicString(" de La Bath"))
    val basicSetB = new BasicSequence(seqB)
    assert(basicSetB.sum().value == "Hubert Bonisseur de La Bath")
  }

  test("Apply") {
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2))
    val basicSet = new BasicSequence(seq)
    assert(basicSet.apply(0).value == 1)
    assert(basicSet.apply(1).value == 2)
  }
}
