package com.codiply.bdpg.akkastreams

import java.time.Instant
import java.util.Date

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import akka.stream.alpakka.cassandra.scaladsl.CassandraSink
import com.codiply.bdpg.constants.{CassandraCluster, KafkaCluster}
import com.codiply.bdpg.model.{AllDeserializers, AllSerdes, WikipediaChange}
import com.datastax.driver.core.{Cluster, PreparedStatement}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object KafkaToCassandra {
  def main(args: Array[String]): Unit = {
    implicit val actorSystem = ActorSystem.create("KafkaToCassandra")
    implicit val materializer = ActorMaterializer()

    val consumerSettings = ConsumerSettings(actorSystem,
      AllDeserializers.stringDeserializer,
      AllSerdes.wikipediaChangeSerde.deserializer()).withBootstrapServers(KafkaCluster.BROKERS.mkString(","))

    val source = createKafkaSource
    val sink = createCassandraSink

    val result = source.map { msg =>
      msg.record.value
    }.filter(x => x.timestamp.isDefined && x.id.isDefined).runWith(sink)

    Await.result(result, Duration.Inf)
  }


  private def createKafkaSource(implicit system: ActorSystem) = {
    val keyDeserialzer = AllDeserializers.stringDeserializer
    val valueDeserializer = AllSerdes.wikipediaChangeSerde.deserializer()
    val consumerSettings = ConsumerSettings(system, keyDeserialzer, valueDeserializer)
      .withBootstrapServers(KafkaCluster.BROKERS.mkString(","))
      .withGroupId("KafkaToCassandra")

    Consumer.committableSource(consumerSettings, Subscriptions.topics(KafkaCluster.Topics.WikipediaChanges))
  }

  private def createCassandraSink(implicit actorSystem: ActorSystem, materializer: ActorMaterializer) = {
    implicit val session = Cluster.builder
      .addContactPoint(CassandraCluster.HOSTS(0))
      .withPort(CassandraCluster.PORT)
      .build
      .connect()

    val family = CassandraCluster.Families.wikipediaChanges

    val preparedStatement = session.prepare(
      s"INSERT INTO ${family.keyspace}.${family.table}(id, timestamp, user, server_name, bot, title)" +
        " VALUES (?, ?, ?, ?, ?, ?)")
    val statementBinder = (c: WikipediaChange, statement: PreparedStatement) => {
      statement.bind()
        .setString(0, c.id.get.toString)
        .setTimestamp(1, Date.from(Instant.ofEpochSecond(c.timestamp.get)))
        .setString(2, c.user)
        .setString(3, c.server_name)
        .setString(4, c.bot.toString)
        .setString(5, c.title)
    }

    CassandraSink[WikipediaChange](parallelism = 2, preparedStatement, statementBinder)
  }
}
