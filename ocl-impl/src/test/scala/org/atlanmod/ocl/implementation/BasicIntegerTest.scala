package org.atlanmod.ocl.implementation

import org.scalatest.funsuite.AnyFunSuite

class BasicIntegerTest extends AnyFunSuite {

  test("Test: value getter should return constructor given value") {
    val basicInteger = new BasicInteger(42)
    assert(basicInteger.value == 42)
  }

}
