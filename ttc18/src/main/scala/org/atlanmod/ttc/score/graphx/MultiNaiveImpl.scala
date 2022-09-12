package org.atlanmod.ttc.score.graphx

object MultiNaiveImpl {
//
//    def allComments(sn: Graph[VertexType, EdgeType], pid: VertexId): VertexRDD[VertexType] = {
//        // TODO (Massimo) : new edges from a post to all its belonging comments!
//        //
//
//        val init_graph = sn.mapVertices((id, value) => (id == pid, value))
//
//        def vprog(id: VertexId, rch: (Boolean, VertexType), newRch: Boolean): (Boolean, VertexType) =
//            if (newRch & !rch._1) {
//                (true, rch._2)
//            } else {
//                rch
//            }
//
//        def sendMsg(triplet: EdgeTriplet[(Boolean, VertexType), EdgeType]): Iterator[(VertexId, Boolean)] =
//            if (triplet.srcAttr._1 & !triplet.dstAttr._1 & triplet.attr == EDGE_COMMENT)
//                Iterator((triplet.dstId, true))
//            else Iterator.empty
//
//        init_graph.pregel(false)(vprog, sendMsg, (a, b) => a | b).vertices
//          // Get the marked element, excluding the post associated to pid
//          .filter(vertex => vertex._2._1 & vertex._1 != pid)
//          // Get only the ids
//          .map(vertex => vertex._1)
//
//
//    }
//
//
//    def countLikes(sn: Graph[VertexType, EdgeType], pid: VertexId): Double = {
//        //        sn.mapTriplets()
//
//        def is_in(vid: VertexId, collection: RDD[VertexId]): Boolean = {
//            false // TODO
//        }
//        //        sn.edges
//        //        sn.vertices
//
//        sn.aggregateMessages(triplet =>
//            if (is_in(triplet.srcId, allComments(sn, pid)) & triplet.attr == EDGE_LIKE) {
//                triplet.sendToSrc(1)
//                //        }, (a, b) => a + b)
//
//                allComments(sn, pid)
//                  .map(cid => sn.triplets.filter(t => t.srcId == cid & t.attr == EDGE_LIKE).count).sum
//                0.0
//            }
//
//
//        def score(sn: Graph[VertexType, EdgeType], p: VertexId, sc: SparkContext = null): Double = {
//            //        10 *10 allComments(sn, p).count + countLikes(sn, p)
//            0.0
//        }

    }
