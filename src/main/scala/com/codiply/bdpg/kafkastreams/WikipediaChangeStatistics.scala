package com.codiply.bdpg.kafkastreams

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId, ZonedDateTime}
import java.util.Properties
import java.util.concurrent.TimeUnit

import com.codiply.bdpg.constants.KafkaCluster
import com.codiply.bdpg.constants.KafkaCluster.Topics
import com.codiply.bdpg.model.{JsonSerde, WikipediaChange}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.{Consumed, KafkaStreams, StreamsBuilder, StreamsConfig}
import spray.json.DefaultJsonProtocol._

object WikipediaChangesStatistics extends LazyLogging {
  import com.codiply.bdpg.model.AllSerdes._

  val applicationId = "wikipedia-change-statistics"

  def main(args: Array[String]): Unit = {
    val builder = new StreamsBuilder()

    val changes: KStream[String, WikipediaChange] =
      builder
        .stream(Topics.WikipediaChanges, Consumed.`with`(stringSerde, wikipediaChangeSerde))

    val window = TimeWindows.of(TimeUnit.MINUTES.toMillis(5)).advanceBy(TimeUnit.MINUTES.toMillis(1))

    changes
      .groupBy(new KeyValueMapper[String, WikipediaChange, String] {
        override def apply(key: String, value: WikipediaChange): String = value.server_name
      }, Serialized.`with`(stringSerde, wikipediaChangeSerde))
      .windowedBy(window)
      .aggregate(
        new Initializer[Set[String]] {
          override def apply(): Set[String] = Set.empty[String]
        },
        new Aggregator[String, WikipediaChange, Set[String]] {
          override def apply(key: String, value: WikipediaChange, aggregate: Set[String]): Set[String] =
            aggregate + value.user
        }, Materialized.`with`(stringSerde, new JsonSerde[Set[String]]()))
        .toStream
        .mapValues[String](_.size.toString)
        .selectKey[String]((key, value) => s"${key.key()} : ${printableTimestamp(key.window().start())} -> ${printableTimestamp(key.window().end())}")
        .to(Topics.WikipediaChangesStatistics)

    val streams = new KafkaStreams(builder.build, buildConfig)
    streams.start()
  }

  def buildConfig(): Properties = {
    val properties = new Properties()
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId)
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaCluster.BROKERS.mkString(","))
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    properties
  }

  def printableTimestamp(epochMillis: Long): String = {
    val instant = Instant.ofEpochMilli(epochMillis)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
    zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  }
}
