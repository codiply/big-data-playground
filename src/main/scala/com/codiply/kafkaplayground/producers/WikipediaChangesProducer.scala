package com.codiply.kafkaplayground.producers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.duration._
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.sse.scaladsl.EventSource
import com.codiply.kafkaplayground.kafka.KafkaCluster.Topics
import com.codiply.kafkaplayground.kafka.StringProducer

import scala.concurrent.Await
import scala.util.parsing.json.JSON

object WikipediaChangesProducer {
  private val changesUrl = "https://stream.wikimedia.org/v2/stream/recentchange"

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create()

    implicit val materializer: Materializer = ActorMaterializer.create(actorSystem)

    val http = Http.get(actorSystem)
    val send = (request: HttpRequest) => http.singleRequest(request)

    val changes = EventSource(Uri(changesUrl), send, None, 1.second)

    val producer = new StringProducer(Topics.WikipediaChanges)

    Await.result(changes.runForeach { serverSentEvent =>
      JSON.parseFull(serverSentEvent.data) match {
        case Some(event: Map[String, Any]) => {
          val key = event("user").toString
          val value = serverSentEvent.data
          Await.result(producer.send(key, value), 10.second)
        }
        case None => println("Parsing failed")
        case other => println("Unknown data structure: " + other)
      }
    }, Duration.Inf)
  }
}
