package org.atlanmod.ttc

import java.io.{File, PrintWriter}
import java.util.Calendar

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Graph, VertexId}
import org.atlanmod.ttc.builder.GraphBuilder.{EdgeType, VertexType}
import org.atlanmod.ttc.builder.{GraphBuilder, ModelBuilder}
import org.atlanmod.zoo.socialnetwork.Post
import org.eclipse.emf.ecore.EObject


object Query {

    final val URI_BASE = "https://raw.githubusercontent.com/TransformationToolContest/ttc2018liveContest/master/models/"
    final val DIR_RES_NAME = "results"
    final val FILE_RES_EXT = "r"
    final val FILE_RES_NAME = "results"

    private def get_local_time : String = {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH)
        val day = now.get(Calendar.DAY_OF_MONTH)
        val hour = now.get(Calendar.HOUR)
        val minute = now.get(Calendar.MINUTE)
        val second =  now.get(Calendar.SECOND)

        year + ":" + month + ":" + day + "_" + hour + ":" + minute + ":" + second
    }

    def get_uris_ttc2018: List[(String, String)] = {
        var res: List[(String, String)] = List()
        for(i <- 9 to 0 by -1) {
            val id_uri = Math.pow(2, i).toInt
            res = (id_uri.toString, URI_BASE + id_uri + "/initial.xmi") :: res
        }
        res
    }

    private def get_posts(model : EObject) : List[Post] = {
        val posts : List[Post] = List()
        for (obj <- model.eContents().toArray()) {
            obj match {
                case post : Post => post :: posts
                case _ =>
            }
        }
        posts
    }

    private def create_r_vector(vector_name : String, data: List[String], asString : Boolean = false): String = {
        var vector_str = vector_name + " <- c("
        for (i <- data.indices) {
            vector_str +=
              (if (asString) "'" + data(i) + "'" else data(i)) + (if (i == data.length - 1) ")" else ",")
        }
        vector_str
    }

    private def write_content(filename: String, content: String): Unit = {
        val pw = new PrintWriter(new File(filename))
        pw.write(content)
        pw.close()
    }

    private def create_if_not_exits(dirname: String) : Unit = {
        val dir : File = new File(dirname);
        if (!dir.exists) dir.mkdirs()

    }

    def test_queries(model: EObject,
                   scores_seq : List[(String, Post => Long)],
                   scores_para : List[(String, (Graph[VertexType, EdgeType], VertexId, SparkContext) => Long)],
                   ntests : Int = 1,  name_uri : String = "0"): String = {

        val meta = ModelBuilder.get_meta(model)
        val nbposts = meta._1
        val nbcomments = meta._2
        val nbusers = meta._3
        val nblikes = meta._4

        val sc = Tool4Spark.getContext

        // TODO print in res the caracteristics of the machine
        var res : String = ""

        val posts : List[Post] = get_posts(model)
        val post_ids : List[VertexId] = posts.map(p => p.getId.toLong)

        var means_vector : List[String] = List()
        var names_vector : List[String] = List()

        // Sequential tests
        for (foo <- scores_seq) {
            val foo_name : String = foo._1
            val name_vector = foo_name + "." + name_uri
            res += name_vector + " <- c("
            for (i <- 1 to ntests){
                val t1 = System.nanoTime
                for (p <- posts){foo._2(p)}
                val duration = (System.nanoTime - t1) * 1000 / 1e9d
                // Print execution time + add it to the final output
                println(foo_name + " (" + i + "/" + ntests + ") on model " + name_uri + ": " + f"$duration%1.5f" + "ms")
                res += f"$duration%1.5f" + (if (i != ntests) "," else ")\n")
            }

            means_vector = ("mean(" + name_vector + ")") :: means_vector
            names_vector = foo_name :: names_vector

        }

        // Init graph from model
        val graph : Graph [GraphBuilder.VertexType, GraphBuilder.EdgeType]= GraphBuilder.build_from_model(model, sc)

        // Parallel tests
        for (foo <- scores_para) {
            val foo_name : String = foo._1
            val name_vector = foo_name + "." + name_uri
            res += name_vector + " <- c("
            for (i <- 1 to ntests){
                val t1 = System.nanoTime
                for (pid <- post_ids){foo._2(graph, pid, sc)}
                val duration = (System.nanoTime - t1) * 1000 / 1e9d
                // Print execution time + add it to the final output
                println(foo_name + " (" + i + "/" + ntests + ") on model " + name_uri + ": " + f"$duration%1.5f" + "ms")
                res += f"$duration%1.5f" + (if (i != ntests) "," else ")\n")
            }
            means_vector = ("mean(" + name_vector + ")") :: means_vector
            names_vector = foo_name :: names_vector
        }

        // Create R barplot (x = foo, y = mean(time))
        val means_vector_name = "means." + name_uri
        val names_vector_name = "names." + name_uri
        res += create_r_vector(means_vector_name, means_vector)+ "\n" +
          create_r_vector(names_vector_name, names_vector, asString = true)+ "\n" +
          "barplot("+ means_vector_name +", names.arg = " + names_vector_name +
          ", main=\"Computation time (ms), dataset = " + name_uri + " \\n" +
          "( "+nbposts+" posts, "+nbcomments+" comments, "+nbusers+" users, "+nblikes+" likes)\")\n"

        res

  }

  def main(args: Array[String]): Unit = {

      val scores_seq = List(
          ("seq.naive", post => org.atlanmod.ttc.score.sequential.NaiveImpl.score(post))
      )

      val scores_para = List(
          ("par.naive", (g, post, sc) => org.atlanmod.ttc.score.graphx.NaiveImpl.score(g, post, sc)),
          ("par.prgel", (g, post, sc) => org.atlanmod.ttc.score.graphx.PregelImpl.score(g, post, sc)),
          ("par.multi", (g, post, sc) => org.atlanmod.ttc.score.graphx.MultiImpl.score(g, post, sc)),
          ("par.mr", (g, post, sc) => org.atlanmod.ttc.score.graphx.MRImpl.score(g, post, sc)),
          ("par.mrpgl", (g, post, sc) => org.atlanmod.ttc.score.graphx.MRPregelImpl.score(g, post, sc))
      )

//      val uris : List[(String, String)] = List(("0", "models/ttc18/0/initial.xmi"))
            val uris : List[(String, String)] =  get_uris_ttc2018
      //      val uris : List[(String, String)] = ("0", "models/ttc18/0/initial.xmi", ()) :: getUrisTTC18

      val ntests = 30


      var res = ""
      for (uri : (String, String) <- uris) {
          val name_uri = uri._1
          val sn = ModelBuilder.build_from_uri(uri._2)
          val tmp_res = test_queries(sn, scores_seq, scores_para, ntests, name_uri)
          val filename = DIR_RES_NAME + "/" + FILE_RES_NAME + "_" + get_local_time + "." + name_uri + "." + FILE_RES_EXT
          write_content(filename, tmp_res)
          res += tmp_res
      }

      create_if_not_exits(DIR_RES_NAME)
      val filename = DIR_RES_NAME + "/" + FILE_RES_NAME + "_" + get_local_time + "." + FILE_RES_EXT
      write_content(filename, res)
    }

}
