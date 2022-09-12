package org.atlanmod.ttc.score.sequential

import org.atlanmod.zoo.socialnetwork.{Comment, Post, Submission}

object NaiveImpl {


    def allComments(p: Post): List[Comment] = {
        def traversal(c: Submission) :  List[Comment] = {
            var res : List[Comment] = c match {
                case comment: Comment => List(comment)
                case _ => List()
            }
            for (comment <- c.getComments.toArray) {
                res = res ++ traversal(comment.asInstanceOf[Comment])
            }
            res
        }
        traversal(p)
    }

    def countLikes(p: Post): Long = {
        allComments(p).map(comment => comment.getLikedBy.size()).sum
    }

    def score(post: Post): Long = {
        10 * allComments(post).size + countLikes(post)
    }
}
