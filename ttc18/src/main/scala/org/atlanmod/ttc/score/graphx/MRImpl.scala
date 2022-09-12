package org.atlanmod.ttc.score.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Graph, VertexId}
import org.atlanmod.ttc.builder.GraphBuilder.{EDGE_COMMENT, EDGE_LIKE, EdgeType, VertexType}

object MRImpl {

    def getScores(sn: Graph[VertexType, EdgeType]): Array[((EdgeType, VertexId), Long)] = {
        // First, we create the following map
        //      key: (type_edge, srcId)
        //      val: 1
        // and reduce it
        //      key: (type_edge, srcId)
        //      val: n
        sn.triplets.filter(t => t.attr == EDGE_LIKE || t.attr == EDGE_COMMENT)
          .map(t => if (t.attr == EDGE_LIKE) ((t.attr, t.srcId), 1L)
          else ((t.attr, t.srcId), 10L))
          .reduceByKey((a, b) => a + b).collect
    }

    def score(sn: Graph[VertexType, EdgeType], p: VertexId, sc: SparkContext = null): Long = {
        val individual_scores = getScores(sn)

        // Need of a traversal to accumulate the total number of like, and of comments, from a source
        def traversal(pid: VertexId): Long = {
            // Get nb of likes and comments for the current vertex.

            val default = (("", ""), 0L)
            // 0 will be assigned to nbLike if there is no key (pid, EDGE_LIKE)
            // (i.e., the current submission is not liked)
            val valLike = individual_scores.find(e => e._1 == (EDGE_LIKE, pid)).getOrElse(default)._2
            // 0 will be assigned to nbCom if there is no key (vid, EDGE_COMMENT)
            // (i.e., the current submission is not commented)
            val valCom = individual_scores.find(e => e._1 == (EDGE_COMMENT, pid)).getOrElse(default)._2
            var current_score = valCom + valLike
            sn.triplets
              // Get all direct comments of p as vertices
              .filter(t => t.srcId == pid & (t.attr == EDGE_LIKE || t.attr == EDGE_COMMENT))
              // collect the ids
              .map(_.dstId).collect
              // recursive application of traversale
              .map(traversal)
              // accumulation of sub number of likes and comment with current values
              .foreach(score => current_score = current_score + score)

            // We return the number of comments and likes
            current_score
        }

        traversal(p)
    }

}
