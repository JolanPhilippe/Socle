package org.atlanmod.ttc.builder

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.atlanmod.zoo.socialnetwork.impl.{PostImpl, UserImpl}
import org.atlanmod.zoo.socialnetwork.{Comment, Submission}
import org.eclipse.emf.ecore.EObject

object GraphBuilder {

    type EdgeType = String
    type VertexType = EObject

    final val EDGE_COMMENT: EdgeType = "comment"
    final val EDGE_LIKE: EdgeType = "like"
    final val EDGE_COMMENTED: EdgeType = "commented"
    final val EDGE_LIKEDBY: EdgeType = "likedBy"
    final val EDGE_SUBMITTER: EdgeType = "submitter"
    final val EDGE_SUBMISSION: EdgeType = "submission"
    final val EDGE_POST: EdgeType = "post"
    final val EDGE_USER: EdgeType = "user"

    private def getAllComments(submission: Submission): (List[(VertexId, Comment)], List[Edge[EdgeType]]) = {
        var comments: List[(VertexId, Comment)] = List()
        var links: List[Edge[EdgeType]] = List()

        for (a_comment <- submission.getComments.toArray()) {
            val comment = a_comment.asInstanceOf[Comment]
            comments = (comment.getId.toLong, comment) :: comments
            links =
              Edge(comment.getId.toLong, submission.getId.toLong, EDGE_COMMENTED) ::
                Edge(submission.getId.toLong, comment.getId.toLong, EDGE_COMMENT) :: links
            val sub = getAllComments(comment)
            comments = comments ++ sub._1
            links = links ++ sub._2
        }
        (comments, links)
    }

    def build_from_model(model: EObject, sc: SparkContext) : Graph[VertexType, EdgeType] = {
        val ID_MODEL = -1L

        var vertices: List[(VertexId, EObject)] = List((ID_MODEL, model))
        var edges: List[Edge[EdgeType]] = List()

        for (obj <- model.eContents().toArray()) {
            obj match {

                case post: PostImpl =>
                    // New entries
                    edges = Edge(ID_MODEL, post.getId.toLong, EDGE_POST) :: edges
                    vertices = (post.getId.toLong, post) :: vertices

                    // Get all belong comments of post
                    val sub = getAllComments(post)
                    vertices = vertices ++ sub._1
                    edges = edges ++ sub._2

                case user: UserImpl =>
                    // New entries
                    edges = Edge(ID_MODEL, obj.asInstanceOf[UserImpl].getId.toLong, EDGE_USER) :: edges
                    vertices = (user.getId.toLong, user) :: vertices

                    // Iterate on his submissions to create the edges
                    for (submission <- user.getSubmissions.toArray) {
                        val sub_id = submission.asInstanceOf[Submission].getId
                        edges =
                          Edge(user.getId.toLong, sub_id.toLong, EDGE_SUBMISSION) ::
                            Edge(sub_id.toLong, user.getId.toLong, EDGE_SUBMITTER) :: edges
                    }

                    // Iterate on his likes to create the edges
                    for (comment <- user.getLikes.toArray) {
                        val com_id = comment.asInstanceOf[Comment].getId
                        edges =
                          Edge(user.getId.toLong, com_id.toLong, EDGE_LIKE) ::
                            Edge(com_id.toLong, user.getId.toLong, EDGE_LIKEDBY) :: edges
                    }

                case _ =>
            }
        }
        Graph(sc.parallelize(vertices), sc.parallelize(edges))
    }

    def build_from_uri(uri: String, sc: SparkContext): Graph[VertexType, EdgeType] = {
        val SN = ModelBuilder.build_from_uri(uri)
        build_from_model(SN, sc)
    }

}
