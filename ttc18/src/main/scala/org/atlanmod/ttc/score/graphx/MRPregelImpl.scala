package org.atlanmod.ttc.score.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{EdgeTriplet, Graph, VertexId}
import org.atlanmod.ttc.builder.GraphBuilder.{EDGE_COMMENT, EDGE_LIKE, EdgeType, VertexType}

object MRPregelImpl {

    def score(sn: Graph[VertexType, EdgeType], pid: VertexId, sc: SparkContext = null): Long = {
        val individual_scores = MRImpl.getScores(sn)
        val score = sc.longAccumulator("score" + pid)

        val default = (("", ""), 0L)

        def vprog(vid: VertexId, value: (Boolean, VertexType), merged_msg: (Boolean, Boolean)): (Boolean, VertexType) = {
            // If a vertex is reached for the first time
            if (merged_msg._1 & !value._1) {
                // 0 will be assigned to nbLike if there is no key (vid, EDGE_LIKE)
                // (i.e., the current submission is not liked)
                val valLike = individual_scores.find(e => e._1 == (EDGE_LIKE, vid)).getOrElse(default)._2
                score.add(valLike)
                // 0 will be assigned to nbCom if there is no key (vid, EDGE_COMMENT)
                // (i.e., the current submission is not commented)
                val valCom = individual_scores.find(e => e._1 == (EDGE_COMMENT, vid)).getOrElse(default)._2
                score.add(valCom)
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
        val valLike = individual_scores.find(e => e._1 == (EDGE_LIKE, pid)).getOrElse(default)._2
        val valCom = individual_scores.find(e => e._1 == (EDGE_COMMENT, pid)).getOrElse(default)._2
        score.add(valLike)
        score.add(valCom)
        score.value
    }

}
