package org.atlanmod.ocl.test

import org.atlanmod.ocl.implementation.BasicString
import org.scalatest.funsuite.AnyFunSuite

class BasicStringTest extends AnyFunSuite {

  test("Test: value getter should return constructor given value") {
    val basicString = new BasicString("John")
    assert(basicString.value == "John")
  }

  // TODO: fix class not found error
  /* A needed class was not found. This could be due to an error in your runpath. Missing class: scala/Function1$class
    java.lang.NoClassDefFoundError: scala/Function1$class
      at org.atlanmod.ocl.implementation.org.atlanmod.ocl.implementation.org.atlanmod.ocl.implementation.first.BasicSet.<init>(BasicSet.scala:5)
      at org.atlanmod.ocl.implementation.org.atlanmod.ocl.implementation.org.atlanmod.ocl.implementation.first.BasicString.allInstances(BasicString.scala:157)
      at BasicStringTest.$anonfun$new$2(BasicStringTest.scala:13) */

  test("Test: allInstances(): Collection[OclAny] should return BasicString.OCL_TYPE_NAME") {
    val basicString = new BasicString("Dummy")
    val oclTypeValue = basicString.allInstances()(0).toString()
    assert(oclTypeValue == basicString.OCL_TYPE_NAME)
  }

}
