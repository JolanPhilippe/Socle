package org.atlanmod.ocl.implementation

import org.atlanmod.ocl.OclAny

object Main extends App {
   override def main(args: Array[String]) = {

    // val filePath = "ocl-impl/data/model.json"

    val seq = Vector[BasicString](
      new BasicString("\"name\":\"Shyam\", \"email\":\"shyamjaiswal@gmail.com\""),
      new BasicString("\"name\":\"Bob\", \"email\":\"bob32@gmail.com\""),
      new BasicString("\"name\":\"Jai\", \"email\":\"jai87@gmail.com\""))

    val basicSet = new BasicSequence(seq)

    val emailQuery = basicSet.forAll(s => s.value.contains("email"))
    println("Is there always an email? " + emailQuery);
    // true

    val bobQuery = basicSet
      .select(s => s.value.contains("Bob"))
      .collect(s => new BasicString(s.value.toUpperCase))
    println("Bob in uppercase: " + bobQuery);
    // BasicSequence("NAME":"BOB", "EMAIL":"BOB32@GMAIL.COM")

  }
}