package com.codiply.bdpg.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{Future, Promise}

class StringProducer(topic: String) {

  private val producer = new KafkaProducer[String, String](buildProperties())

  def send(key: String, value: String): Future[RecordMetadata] = {
    val record = new ProducerRecord[String, String](topic, key, value)

    val promise = Promise[RecordMetadata]()

    producer.send(record, new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        if (exception == null) {
          promise.success(metadata)
        } else {
          promise.failure(exception)
        }
      }
    })

    promise.future
  }

  private def buildProperties(): Properties = {
    val props = new Properties()

    props.put("bootstrap.servers", KafkaCluster.BROKERS.mkString(","))

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("acks", "1")
    props.put("producer.type", "async")
    props.put("retries", "3")

    props
  }
}
