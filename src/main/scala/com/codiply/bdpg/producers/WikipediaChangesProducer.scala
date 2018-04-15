package com.codiply.bdpg.producers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.stream.{ActorMaterializer, Materializer}
import com.codiply.bdpg.constants.KafkaCluster.Topics
import com.codiply.bdpg.kafka.StringProducer
import com.codiply.bdpg.model.WikipediaChange
import com.typesafe.scalalogging.LazyLogging
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._

object WikipediaChangesProducer extends LazyLogging {
  import com.codiply.bdpg.model.JsonProtocol._

  private val changesUrl = "https://stream.wikimedia.org/v2/stream/recentchange"

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem.create("WikipediaChangesProducer")

    implicit val materializer: Materializer = ActorMaterializer.create(actorSystem)

    val http = Http.get(actorSystem)
    val send = (request: HttpRequest) => http.singleRequest(request)

    val changes = EventSource(Uri(changesUrl), send, None, 1.second)

    val producer = new StringProducer(Topics.WikipediaChanges)

    val result = changes.runForeach { serverSentEvent =>
      try {
        val change = serverSentEvent.data.parseJson.convertTo[WikipediaChange]
        producer.send(change.user, change.toJson.toString())
      } catch {
        case e: Exception => logger.error(s"Error parsing event.", e)
      }
    }

    Await.result(result, Duration.Inf)
  }
}
