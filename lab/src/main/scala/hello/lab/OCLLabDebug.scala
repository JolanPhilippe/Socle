package hello.lab

import hello.graphx.ReachabilityFromSource.sc
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph}

object OCLLabDebug extends Serializable {
  type EdgeValue = String
  type VertexValue = Long

  final val EDGE_COMMENT = "Comment"
  final val EDGE_LIKE = "Like"
  final val EDGE_OTHER = "Other"
  //  val EDGE_COMMENT = 1L
  //  val EDGE_LIKE = 2L
  //  val EDGE_OTHER = 3L

  def graph(sc: SparkContext): Graph[Long, EdgeValue] = {
    val vertices = sc.parallelize(List(
      (0L,0L),(1L,1L),(2L,2L),(3L,3L),(4L,4L),(5L,5L)
    ))
    val edges = sc.parallelize(List(
      Edge(0L,1L,EDGE_COMMENT),Edge(0L,2L,EDGE_COMMENT),
      Edge(1L,3L,EDGE_LIKE),Edge(1L,4L,EDGE_LIKE),
      Edge(0L, 5L, EDGE_OTHER)
    ))
    Graph(vertices, edges)
  }

  def printsn(sn : Graph[VertexValue, EdgeValue]): Unit = {
    val tt = sn.triplets
    if (tt != null)
      tt.foreach(t => {print(t.srcAttr);print(" => ");print(t.attr);print(" => ");print(t.dstAttr);println()})
    else
      print("NULL")
  }

  def browsingFromSource(sn : Graph[VertexValue, EdgeValue]) : Unit = {
    val p = 0L
    val ss = 0

    def browse(sn : Graph[VertexValue, EdgeValue], c : VertexValue, ss: Int): List[VertexValue] = {
      print("REC "); print(ss); print(" test on:"); println(c)

      val sub = sn.triplets.filter(t => t.srcId == c && (t.attr == EDGE_COMMENT | t.attr == EDGE_LIKE)).map(_.dstId).collect

      sub.foreach(a => {
        print("Sub of " + c + " is ")
        println(a)
      })
      val sub_comments = sub.flatMap(a => browse(sn, a, ss + 1))
      sub_comments.foreach(println(_))
      List(c).union(sub_comments)
      //      }
//      sn.triplets.filter(t => t.srcAttr == c).map(_.dstAttr).map(v => browse(sn, v, ss+1)).collect()
//      a
    }

    val all_comments = browse(sn, p, ss)
    print(all_comments)
  }


  def score_ocl(sn: Graph[VertexValue, EdgeValue]): Long = {
    val p = 0L

    def allComments(sn : Graph[VertexValue, EdgeValue], c : VertexValue) : List[VertexValue] =
      List(c).union(sn.triplets.filter(t => t.srcAttr == c && t.attr == EDGE_COMMENT)
        .map(_.dstAttr).map(v => allComments(sn, v)).flatMap(identity).collect)

    def countLikes(sn : Graph[VertexValue, EdgeValue], p: VertexValue) =
      allComments(sn, p).map(c => sn.triplets.filter(t => t.attr == EDGE_LIKE && t.dstAttr == c).count).sum

    10 * allComments(sn, p).size + countLikes(sn, p)
  }

  def main(args: Array[String]): Unit = {
    val generated_graph = graph(sc)
//    val res = score_ocl(generated_graph)
    browsingFromSource(generated_graph)
  }
}
