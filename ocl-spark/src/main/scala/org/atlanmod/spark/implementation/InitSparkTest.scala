package org.atlanmod.spark.implementation

object InitSparkTest extends App with InitSpark {
  override def main(args: Array[String]) = {

    val logFile = "ocl-spark/data/demo.txt" // Should be some file on your system

    println("First SparkContext:")
    println("APP Name: " + spark.sparkContext.appName)
    println("Deploy Mode: " + spark.sparkContext.deployMode)
    println("Master: " + spark.sparkContext.master)

    val logData = spark.read.textFile(logFile).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()
  }
}