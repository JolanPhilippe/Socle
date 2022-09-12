package hello.graphx

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.graphx.{Edge, EdgeTriplet, Graph, VertexId}

object ReachabilityFromSource {

  val conf = new SparkConf()
  conf.setAppName("Lab")
  conf.setMaster("local")
  val sc = new SparkContext(conf)
  sc.setLogLevel("OFF")

  def rch(g: Graph[Long, Double], source: VertexId, limit: Long): Graph[Boolean, Double] = {
    var counter = sc.longAccumulator("Counter of reached vertices")
    counter.add(1L)

    def vprog(id: VertexId, rch: Boolean, newRch: Boolean): Boolean = {
      //      println("activate " + id)
      if (newRch & newRch != rch & counter.value < limit) {
        counter.add(1L);
        newRch
      } else rch
    }

    def sendMsg(triplet: EdgeTriplet[Boolean, Double]) =
      if (triplet.srcAttr & !triplet.dstAttr & counter.value < limit) {
        Iterator((triplet.dstId, triplet.srcAttr))
        //        println("A message has been sent to " + triplet.dstId)
      }
      else Iterator.empty


    def mergeMsg(a: Boolean, b: Boolean) : Boolean = a | b

    val initialGraph = g.mapVertices((id, _) => (id == source))
    val res = initialGraph.pregel(initialMsg = false)(vprog, sendMsg, mergeMsg)
    res
  }



    def rch(g: Graph[Long, Double], source: VertexId): Graph[Boolean, Double] = {
      def vprog(id: VertexId, rch: Boolean, newRch: Boolean): Boolean = {
        //      println("activate " + id)
        if (newRch && newRch != rch) newRch else rch
      }

      def sendMsg(triplet: EdgeTriplet[Boolean, Double]) =
        if (triplet.srcAttr & !triplet.dstAttr) {
          Iterator((triplet.dstId, triplet.srcAttr))
          //        println("A message has been sent to " + triplet.dstId)
        }
        else Iterator.empty

      def mergeMsg(a: Boolean, b: Boolean): Boolean = a | b

      val initialGraph = g.mapVertices((id, _) => if (id == source) true else false)
      initialGraph.pregel(initialMsg = false)(vprog, sendMsg, mergeMsg)
    }

    def graph0(sc: SparkContext) = {
      val vertices = sc.parallelize(List(
        (0L, 0L), (1L, 1L), (2L, 2L), (3L, 3L), (4L, 4L)
      ))
      val edges = sc.parallelize(List(
        Edge(0L, 1L, 0d), Edge(0L, 2L, 0d), Edge(1L, 3L, 0d), Edge(2L, 3L, 0d), Edge(4L, 3L, 0d)
      ))
      val graph = Graph(vertices, edges, 0L)
      val source = 0
      (graph, source)
    }

    def graph1(sc: SparkContext) = {
      val graph: Graph[Long, Double] = GraphGenerators.logNormalGraph(sc, numVertices = 50).mapEdges(e => 0)
      val source: VertexId = 42 // The ultimate source
      (graph, source, 0L)
    }

    def main(args: Array[String]): Unit = {
      // A graph with edge attributes
      val generated_graph = graph0(sc)
      // Initialize the graph such that all vertices except the root have distance infinity.
      //    val res = rch(generated_graph._1, generated_graph._2)
      val res = rch(generated_graph._1, generated_graph._2, 1)
      for (v <- res.vertices) {
        print("The vertex " + v._1 + " is " + (if (v._2) "reached" else "not reached") + "\n")
      }
    }

  }
