package hello.graphx

import org.apache.spark.{SparkConf, SparkContext}

object Tool {
  def getContext(): SparkContext ={
    val conf = new SparkConf()
    conf.setAppName("Lab")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("OFF")
    sc
  }
}
