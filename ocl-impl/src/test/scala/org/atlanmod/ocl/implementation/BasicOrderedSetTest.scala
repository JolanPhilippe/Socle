package org.atlanmod.ocl.implementation

import org.scalatest.funsuite.AnyFunSuite

class BasicOrderedSetTest extends AnyFunSuite {

  test("Union"){
    val seqA = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(4))
    val seqB = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(3), new BasicInteger(4))
    val seqAUnionB = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3), new BasicInteger(4))
    val basicSetA = new BasicOrderedSet(seqA)
    val basicSetB = new BasicOrderedSet(seqB)
    val union = basicSetA.union(basicSetB)
    assert(seqAUnionB.length == union.length)
  }

   test("Collect"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(4), new BasicInteger(8))
    val basicSeqA = new BasicOrderedSet(a)
    var squareFun = (a: BasicInteger) => (new BasicInteger((a.value * a.value)))
    var squaredSeq = basicSeqA.collect(squareFun)
    var i = 0
    for (i <- 0 to basicSeqA.length - 1) {
      assert(basicSeqA.at(i).value * basicSeqA.at(i).value == squaredSeq(i).value)
    }
  }

  test("Select"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeqA = new BasicOrderedSet(a)
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var selectedSet = basicSeqA.select(isEvenFun)
    assert(selectedSet.length == 1)
  }

   test("Reject"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeqA = new BasicOrderedSet(a)
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var rejectedSet = basicSeqA.reject(isEvenFun)
    assert(rejectedSet.length == 2)
  }

  test("ForAll"){
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSeq = new BasicOrderedSet(seq)
    val res = basicSeq.forAll(s => s > new BasicInteger(Int.MaxValue))
    assert(res == false)

    val resB = basicSeq.forAll(s => s == new BasicInteger(1))
    assert(resB == false)

    val resC = basicSeq.forAll(s => s != new BasicInteger(9))
    assert(resC == true)

  }

  test("IndexOf"){
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicOrderedSet(seq)
    assert(basicSet.indexOf(new BasicInteger(3)) == 2)
  }

   test("At"){
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicOrderedSet(seq)
    assert(basicSet.at(0).value == 1)
    assert(basicSet.at(1).value == 2)
    assert(basicSet.at(2).value == 3)
  }

   test("SubOrderedSet"){
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3), new BasicInteger(4))
    val subSeq = Vector[BasicInteger](new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicOrderedSet(seq)
    val basicSubSet = basicSet.subOrderedSet(1, 2)
    assert(basicSubSet.at(0).value == 2)
    assert(basicSubSet.at(1).value == 3)
  }

   test("InsertAt"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(3))
    val basicSet = new BasicOrderedSet(a)

    // middle insertion
    val basicSetB = basicSet.insertAt(1, new BasicInteger(2))
    assert(basicSetB.length == 3)
    assert(basicSetB.at(0).value == 1 && basicSetB.at(1).value == 2 && basicSetB.at(2).value == 3)

    // end insertion
    val basicSetC = basicSetB.insertAt(3, new BasicInteger(4))
    assert(basicSetC.length == 4)
    assert(basicSetC.at(0).value == 1 && basicSetC.at(1).value == 2 &&
      basicSetC.at(2).value == 3 && basicSetC.at(3).value == 4)

    // beginning insertion
    val basicSetD = basicSetC.insertAt(0, new BasicInteger(0))
    assert(basicSetD.length == 5)
    assert(basicSetD.at(0).value == 0 && basicSetD.at(1).value == 1 && basicSetD.at(2).value == 2 &&
      basicSetD.at(3).value == 3 && basicSetD.at(4).value == 4)
  }

   test("Append"){
    val a = Vector[BasicInteger](new BasicInteger(2))
    val basicSet = new BasicOrderedSet(a)
    val basicSetB = basicSet.append(new BasicInteger(3))
    assert(basicSetB.length == 2 && basicSetB.at(0).value == 2 && basicSetB.at(1).value == 3)
  }

   test("Prepend"){
    val a = Vector[BasicInteger](new BasicInteger(2))
    val basicSet = new BasicOrderedSet(a)
    val basicSetB = basicSet.prepend(new BasicInteger(1))
    assert(basicSetB.length == 2 && basicSetB.at(0).value == 1 && basicSetB.at(1).value == 2)
  }

  test("Including"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicOrderedSet(a)
    val basicSetB = basicSet.including(new BasicInteger(4))
    assert(basicSetB.length == 4 && basicSetB.at(3).value == 4)
    val basicSetC = basicSet.including(new BasicInteger(3))
    assert(basicSetC.length == 3)
  }

  test("Excluding"){
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSet = new BasicOrderedSet(a)
    val basicSetB = basicSet.excluding(new BasicInteger(2))
    assert(basicSetB.length == 2 && basicSetB.at(0).value == 1 && basicSetB.at(1).value == 3)
  }

}
