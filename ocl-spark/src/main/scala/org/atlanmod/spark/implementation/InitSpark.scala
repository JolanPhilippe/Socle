package org.atlanmod.spark.implementation

import org.apache.log4j.{Level, LogManager, Logger}
import org.apache.spark.sql.SparkSession

/**
  * @author Faizan Ahemad
  * https://github.com/faizanahemad/spark-gradle-template/blob/master/src/main/scala/template/spark/InitSpark.scala
  *
  * Classes that need a Spark Context only need to extend this trait, which gives them access to a spark context
  * in a kind of singleton pattern. This way, those classes don't need to instantiate a Spark Session.
  */
trait InitSpark {
  val spark: SparkSession = SparkSession.builder().appName("Spark example").master("local[*]")
    .config("spark.some.config.option", "some-value").getOrCreate()
  val sc = spark.sparkContext
  val sqlContext = spark.sqlContext
  def reader = spark.read.option("header",true).option("inferSchema", true).option("mode", "DROPMALFORMED")
  def readerWithoutHeader = spark.read.option("header",true).option("inferSchema", true).option("mode", "DROPMALFORMED")
  private def init = {
    sc.setLogLevel("ERROR")
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    LogManager.getRootLogger.setLevel(Level.ERROR)
  }
  init
  def close = {
    spark.close()
  }
}
