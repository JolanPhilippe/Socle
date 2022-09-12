package hello.graphx

import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.graphx.{EdgeTriplet, Graph, VertexId}

object SingleSourceShortestPath {

  def sssp(graph: Graph[Long, Double], source: VertexId) : Unit = {

      def vprog(id: VertexId, dist: Double, newDist: Double) =  {
        println("activate " + id)
        if (newDist != Double.PositiveInfinity)
          println("received message: " + newDist)
        math.min(dist, newDist)
      }

      def sendMsg(triplet: EdgeTriplet[Double, Double]) =
        if (triplet.srcAttr + triplet.attr < triplet.dstAttr){
          println("A message has been sent to " + triplet.dstId)
          Iterator((triplet.dstId, triplet.srcAttr + triplet.attr))
        }
        else Iterator.empty

      def mergeMsg(a: Double, b: Double) : Double = math.min(a, b)

      val initialGraph = graph.mapVertices((id, _) =>
        if (id == source) 0.0 else Double.PositiveInfinity
      )
      initialGraph.pregel(Double.PositiveInfinity)(vprog, sendMsg, mergeMsg)
  }

  def main(args: Array[String]): Unit = {
    val sc = Tool.getContext()
    // A graph with edge attributes containing distances
    val graph: Graph[Long, Double] =
      GraphGenerators.logNormalGraph(sc, numVertices = 50).mapEdges(e => math.pow(e.attr.toDouble,10))
    val sourceId: VertexId = 42 // The ultimate source
    // Initialize the graph such that all vertices except the root have distance infinity.
    val res = sssp(graph, sourceId)
//    print(res)
  }

}
