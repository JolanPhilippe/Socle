package hello.lab

import hello.graphx.ReachabilityFromSource.sc
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, EdgeTriplet, Graph, VertexId}
import org.apache.spark.rdd.RDD

object PregelLabDebug {
  type EdgeValue = String
  // type EdgeValue = Int

  final val EDGE_COMMENT = "Comment"
  final val EDGE_LIKE = "Like"
  final val EDGE_OTHER = "Other"
//  val EDGE_COMMENT = 1L
//  val EDGE_LIKE = 2L
//  val EDGE_OTHER = 3L

  def graph(sc: SparkContext): Graph[Long, EdgeValue] = {
    val vertices : RDD[(VertexId, Long)] = sc.parallelize(List(
      (0L,0L),(1L,1L),(2L,2L),(3L,3L),(4L,4L),(5L,5L)
    ))
    val edges : RDD[Edge[EdgeValue]] = sc.parallelize(List(
      Edge(0L,1L,EDGE_COMMENT),Edge(0L,2L,EDGE_COMMENT),
      Edge(1L,3L,EDGE_LIKE),Edge(1L,4L,EDGE_LIKE),
      Edge(0L,5L,EDGE_OTHER)
    ))
    Graph(vertices, edges)
  }

  def score_pregel(sn: Graph[Long, EdgeValue]): Graph[(Long, Boolean), EdgeValue] = {
    val fstId = 0L

    def vprog(id: VertexId, rch: (Long, Boolean), newRch: Boolean) =
      if (newRch & newRch != rch._2){ (rch._1, newRch) } else { rch }

    def sendMsg(triplet: EdgeTriplet[(Long, Boolean), EdgeValue])  =
      if (triplet.srcAttr._2 & !triplet.dstAttr._2
        & (triplet.attr == EDGE_COMMENT || triplet.attr == EDGE_LIKE)
      ) {
        Iterator((triplet.dstId, triplet.srcAttr._2))
      }
       else Iterator.empty

    val initialGraph = sn.mapVertices((id, value) =>  if (id == fstId) (value, true) else (value, false))
    val res = initialGraph.pregel(initialMsg = false)(vprog, sendMsg, (a,b) => a | b)
    res
  }

  def main(args: Array[String]): Unit = {
    val generated_graph = graph(sc)
    val res = score_pregel(generated_graph)
    for (v <- res.vertices){
      print("The vertex " + v._1 + " is " + (if (v._2._2) "reached" else "not reached") + "\n")
    }
  }
}
