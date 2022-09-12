package org.atlanmod.ttc.score.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeTriplet, Graph, VertexId}
import org.atlanmod.ttc.builder.GraphBuilder.{EDGE_COMMENT, EDGE_LIKE, EdgeType, VertexType}

object PregelImpl {

    def score(sn: Graph[VertexType, EdgeType], pid: VertexId, sc: SparkContext = null): Long = {

        val score = sc.longAccumulator("score" + pid)

        /*
    messages = (id that should compute, boolean to describe what accumulator must be incremented)
    If message._1: vertex is reached
    About the boolean value:
       True -> it is a like, then: score + 1
       False -> it is a comment, then: score + 10
    */

        def vprog(vid: VertexId, value: (Boolean, VertexType), merged_msg: (Boolean, Boolean)): (Boolean, VertexType) = {
            if (merged_msg._1 & !value._1) {
                // If a vertex is reached for the first time, we test what kind of edge has been used
                //  True -> it is a like, then: score + 1
                //  False -> it is a comment, then: score + 10
                if (merged_msg._2) score.add(1L)
                else score.add(10L)
                // turn value._1 to true: the vertex is now reached
                (true, value._2)
            } else {
                // Either it is still not reached, or already reached before.
                value
            }
        }

        def sendMsg(triplet: EdgeTriplet[(Boolean, VertexType), EdgeType]): Iterator[(VertexId, (Boolean, Boolean))] = {
            var res: Iterator[(VertexId, (Boolean, Boolean))] = Iterator.empty
            if (triplet.srcAttr._1 & !triplet.dstAttr._1) {
                if (triplet.attr == EDGE_COMMENT)
                    res = Iterator((triplet.dstId, (true, false)))
                if (triplet.attr == EDGE_LIKE)
                    res = Iterator((triplet.dstId, (true, true)))
            }
            res
        }

        def mergeMsg(m1: (Boolean, Boolean), m2: (Boolean, Boolean)) = (m1._1 | m2._1, m1._1)

        val initialGraph = sn.mapVertices((id, v) => (id == pid, v))
        initialGraph.pregel(initialMsg = (false, false))(vprog, sendMsg, mergeMsg)
        score.value
    }

}
