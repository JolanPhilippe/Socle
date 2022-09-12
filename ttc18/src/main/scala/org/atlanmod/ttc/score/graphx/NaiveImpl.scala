package org.atlanmod.ttc.score.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Graph, VertexId}
import org.atlanmod.ttc.builder.GraphBuilder.{EDGE_COMMENT, EDGE_LIKE, EdgeType, VertexType}

object NaiveImpl {

    def allComments(sn: Graph[VertexType, EdgeType], pid: VertexId): List[VertexId] = {
        def traversal(pid: VertexId): List[VertexId] = {
            val sub =
                sn.triplets
                  .filter(t => t.srcId == pid & t.attr == EDGE_COMMENT) // Get all direct comments of p as vertices
                  .map(_.dstId).collect // Collect the ids
                  .flatMap(a => traversal(a)) // recursive application
            List(pid).union(sub)
        }

        traversal(pid).drop(1) // remove the pid corresponding to the post (only keep comments)
    }

    def countLikes(sn: Graph[VertexType, EdgeType], pid: VertexId): Long = {
        allComments(sn, pid)
          .map(cid =>
              sn.triplets.filter(t => t.srcId == cid & t.attr == EDGE_LIKE)
                .count)
          .sum
    }

    def score(sn: Graph[VertexType, EdgeType], p: VertexId, sc: SparkContext = null): Long = {
        10 * allComments(sn, p).size + countLikes(sn, p)
    }

}
