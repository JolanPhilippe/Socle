package org.atlanmod.ttc.score.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeTriplet, Graph, VertexId}
import org.atlanmod.ttc.builder.GraphBuilder.{EDGE_COMMENT, EDGE_LIKE, EdgeType, VertexType}

object MultiImpl {

    def allComments(sn: Graph[VertexType, EdgeType], pid: VertexId): Array[VertexId] = {
        val init_graph = sn.mapVertices((id, value) => (id == pid, value))

        def vprog(id: VertexId, rch: (Boolean, VertexType), newRch: Boolean): (Boolean, VertexType) =
            if (newRch & !rch._1) {
                (true, rch._2)
            } else {
                rch
            }

        def sendMsg(triplet: EdgeTriplet[(Boolean, VertexType), EdgeType]): Iterator[(VertexId, Boolean)] =
            if (triplet.srcAttr._1 & !triplet.dstAttr._1 & triplet.attr == EDGE_COMMENT)
                Iterator((triplet.dstId, true))
            else Iterator.empty

        init_graph.pregel(false)(vprog, sendMsg, (a, b) => a | b).vertices
          // Get the marked element, excluding the post associated to pid
          .filter(vertex => vertex._2._1 & vertex._1 != pid)
          // Get only the ids
          .map(vertex => vertex._1).collect
    }


    def countLikes(sn: Graph[VertexType, EdgeType], pid: VertexId, comments: Array[VertexId]): Long = {
        allComments(sn, pid).map(cid => sn.triplets.filter(t => t.srcId == cid & t.attr == EDGE_LIKE).count).sum
        //        comments.map(cid =>sn.triplets.filter(t => t.srcId == cid & t.attr == EDGE_LIKE).count).sum
    }


    def score(sn: Graph[VertexType, EdgeType], p: VertexId, sc: SparkContext = null): Long = {
        val comments = allComments(sn, p)
        10 * comments.length + countLikes(sn, p, comments)
    }

}
