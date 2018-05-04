package com.codiply.bdpg.akkastreams

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}
import com.aerospike.client.policy.WritePolicy
import com.aerospike.client.{AerospikeClient, Bin, IAerospikeClient, Key}
import com.codiply.bdpg.constants.{AerospikeCluster, KafkaCluster}
import com.codiply.bdpg.model.{AllDeserializers, AllSerdes, WikipediaChange}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object KafkaToAerospike {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem.create("KafkaToCassandra")
    implicit val materializer = ActorMaterializer()

    val consumerSettings = ConsumerSettings(actorSystem,
      AllDeserializers.stringDeserializer,
      AllSerdes.wikipediaChangeSerde.deserializer()).withBootstrapServers(KafkaCluster.BROKERS.mkString(","))

    val aerospikeClient = new AerospikeClient(AerospikeCluster.HOSTS.head, AerospikeCluster.PORT)

    val source = createKafkaSource
    val sink = createAerospikeSink(aerospikeClient)

    val result = source.map { msg =>
      msg.record.value
    }.runWith(sink)

    Await.result(result, Duration.Inf)
  }


  private def createKafkaSource(implicit system: ActorSystem) = {
    val keyDeserialzer = AllDeserializers.stringDeserializer
    val valueDeserializer = AllSerdes.wikipediaChangeSerde.deserializer()
    val consumerSettings = ConsumerSettings(system, keyDeserialzer, valueDeserializer)
      .withBootstrapServers(KafkaCluster.BROKERS.mkString(","))
      .withGroupId("KafkaToAerospike")

    Consumer.committableSource(consumerSettings, Subscriptions.topics(KafkaCluster.Topics.WikipediaChanges))
  }

  private def createAerospikeSink(client: IAerospikeClient)(implicit actorSystem: ActorSystem, materializer: ActorMaterializer) =
    Sink.foreach[WikipediaChange] { change =>
      (change.id, change.timestamp) match {
        case (Some(id), Some(t)) =>
          val policy = new WritePolicy()
          policy.expiration = 60 * 60

          val key = new Key("wikipedia", "changes", id)
          val user = new Bin("user", change.user)
          val serverName = new Bin("server_name", change.server_name)
          val title = new Bin("title", change.title)
          val bot = new Bin("bot", change.bot)
          val timestamp = new Bin("timestamp", t)

          client.put(policy, key, user, serverName, title, bot, timestamp)
        case _ => ()
      }
    }
}
