package org.atlanmod.ocl.test

import org.atlanmod.ocl.implementation.{BasicInteger, BasicSet}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable.ArrayBuffer

class BasicSetTest extends AnyFunSuite {

  test("Test Union") {
    var x = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2),
      new BasicInteger(3)))
    var y = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2)))

    assert(x.union(y).length == 3)
    //    println("Result "+ x +" intersection "+ y +" = "+ x.union(y))
  }

  test("Test Intersection") {
    var x = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2),
      new BasicInteger(3)))
    var y = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2)))

    assert(x.intersection(y).length == 2)
    //    println("Result "+ x +" intersection "+ y +" = "+ x.intersection(y))
  }

  test("Test Difference") {
    var x = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2),
      new BasicInteger(3)))
    var y = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2)))

    assert(x.-(y).length == 1)
    //    println("Result "+ x +" - "+ y +" = "+ x.-(y))
  }

  test("Test Including") {
    var x = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2),
      new BasicInteger(3)))
    var a = new BasicInteger(3)
    var b = new BasicInteger(4)

    assert(x.including(a).length == 3)
    //    println("Result "+ x +" including "+ a +" = "+ x.including(a))
    assert(x.including(b).length == 4)
    //    println("Result "+ x +" including "+ b +" = "+ x.including(b))
  }

  test("Test Excluding") {
    var x = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2),
      new BasicInteger(3)))
    var a = new BasicInteger(3)
    var b = new BasicInteger(4)

    assert(x.excluding(a).length == 2)
    //    println("Result "+ x +" excluding "+ a +" = "+ x.excluding(a))
    assert(x.excluding(b).length == 3)
    //    println("Result "+ x +" excluding "+ b +" = "+ x.excluding(b))
  }

  test("Test SymmetricDifference") {
    var x = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(2),
      new BasicInteger(3)))
    var y = new BasicSet[BasicInteger](ArrayBuffer[BasicInteger](new BasicInteger(1),
      new BasicInteger(4),
      new BasicInteger(5)))

    assert(x.symmetricDifference(y).length == 4)
    //    println("Result "+ x +" symmetricDifference "+ y +" = "+ x.symmetricDifference(y))
  }

  test("Test For All") {
    val seq = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(1), new BasicInteger(3), new BasicInteger(1))
    val basicSet = new BasicSet(seq)
    val res = basicSet.forAll(s => s > new BasicInteger(Int.MaxValue))
    assert(res == false)

    val resB = basicSet.forAll(s => s == new BasicInteger(1))
    assert(resB == false)

    val resC = basicSet.forAll(s => s != new BasicInteger(9))
    assert(resC == true)
  }

  test("Test Collect") {
    var basicIntValue = new BasicInteger(8)
    val a = Vector[BasicInteger](basicIntValue)
    val basicSetA = new BasicSet(a)
    var squareFun = (a: BasicInteger) => (new BasicInteger((a.value * a.value)))
    var squaredSet = basicSetA.collect(squareFun)
    squaredSet.foreach(e => assert(e.value == basicIntValue.value * basicIntValue.value))
  }

  test("Test Select") {
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSetA = new BasicSet(a)
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var selectedSet = basicSetA.select(isEvenFun)
    assert(selectedSet.length == 1)
  }

  test("Test Reject") {
    val a = Vector[BasicInteger](new BasicInteger(1), new BasicInteger(2), new BasicInteger(3))
    val basicSetA = new BasicSet(a)
    var isEvenFun = (a: BasicInteger) => (a.value % 2 == 0)
    var rejectedSet = basicSetA.reject(isEvenFun)
    assert(rejectedSet.length == 2)
  }

}
