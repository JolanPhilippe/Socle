package org.atlanmod.ttc
import org.apache.spark.{SparkConf, SparkContext}

object Tool4Spark {

  private var sc : SparkContext = null

  def getContext: SparkContext = {
    if (sc == null) {
      val conf = new SparkConf()
      conf.setAppName("Lab")
      conf.setMaster("local")
      sc = new SparkContext(conf)
      sc.setLogLevel("OFF")
    }
    sc
  }

}
