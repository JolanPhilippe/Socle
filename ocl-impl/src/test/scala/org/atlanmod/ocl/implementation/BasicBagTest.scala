package org.atlanmod.ocl.implementation

import org.scalatest.funsuite.AnyFunSuite


class BasicBagTest extends AnyFunSuite {

  test("ForAll"){
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(1), new BasicInteger(3), new BasicInteger(1))
    val basicBag = new BasicBag(seq)
    val res = basicBag.forAll(s => s > new BasicInteger(Int.MaxValue))
    assert(res == false)

    val resB = basicBag.forAll(s => s == new BasicInteger(1))
    assert(resB == false)

    val resC = basicBag.forAll(s => s != new BasicInteger(9))
    assert(resC == true)
  }

   test("Including"){
    val a = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(2), new BasicInteger(1))
    val basicBag = new BasicBag(a)
    val basicBagB = basicBag.including(new BasicInteger(4))
    assert(basicBagB.length == 4 && basicBagB.contains(new BasicInteger(4)))
  }

   test("Excluding"){
    val a = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(1), new BasicInteger(2))
    val basicBag = new BasicBag(a)
    val basicBagB = basicBag.excluding(new BasicInteger(2))
    assert(basicBagB.length == 1 && !basicBagB.contains(new BasicInteger(2)))
  }

  test("Collect"){
    var basicIntValue = new BasicInteger(8)
    val a = Vector[BasicInteger](basicIntValue)
    val basicSetA = new BasicBag(a)
    var squareFun = (a: BasicInteger) => (new BasicInteger((a.value * a.value)))
    var squaredSet = basicSetA.collect(squareFun)
    squaredSet.foreach(e => assert(e.value == basicIntValue.value * basicIntValue.value))
  }

   test("Select"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeqA = new BasicBag(a)
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var selectedSet = basicSeqA.select(isEvenFun)
    assert(selectedSet.length == 1)
  }

  test("Reject"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeqA = new BasicBag(a)
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var rejectedSet = basicSeqA.reject(isEvenFun)
    assert(rejectedSet.length == 2)
  }

}
