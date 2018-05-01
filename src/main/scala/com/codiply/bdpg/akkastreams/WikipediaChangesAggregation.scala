package com.codiply.bdpg.akkastreams

import java.nio.file.Paths
import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString
import com.codiply.bdpg.constants.KafkaCluster
import com.codiply.bdpg.model.{AllDeserializers, AllSerdes}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object WikipediaChangesAggregation {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem.create("WikipediaChangesAggregation")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val source = createKafkaSource

    val windows = Source.tick(0 seconds, 10 seconds, None)
      .map{ _ => LocalDateTime.now()}
      .sliding(2)
      .map(w => (w(0), w(1)))

    val result = source
      .map(_.record.value)
      .filter(_.timestamp.isDefined)
      .conflateWithSeed { change =>
        Map(change.server_name -> Set(change.user))
      } { (state, change) =>
        val serverName = change.server_name
        val user = change.user
        val newUsers = state.get(serverName) match {
          case Some(users) => users + user
          case None => Set(user)
        }
        state + (serverName -> newUsers)
      }
      .zipWith(windows)(Keep.both)
      .mapConcat{ case (state, window) =>
        state.map { case (serverName, users) => s"${window._1} -> ${window._2} -> $serverName -> ${users.size}" }
      }.runWith(lineSink("/data/wikipedia-changes-aggregation.txt"))

    Await.result(result, Duration.Inf)
  }

  private def createKafkaSource(implicit system: ActorSystem) = {
    val keyDeserialzer = AllDeserializers.stringDeserializer
    val valueDeserializer = AllSerdes.wikipediaChangeSerde.deserializer()
    val consumerSettings = ConsumerSettings(system, keyDeserialzer, valueDeserializer)
      .withBootstrapServers(KafkaCluster.BROKERS.mkString(","))
      .withGroupId("WikipediaChangesAggregation")

    Consumer.committableSource(consumerSettings, Subscriptions.topics(KafkaCluster.Topics.WikipediaChanges))
  }

  private def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
}
