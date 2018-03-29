package com.codiply.kafkaplayground.kafka

object KafkaCluster {
  val BROKERS: List[String] = List("kafka-1:9092", "kafka-2:9092", "kafka-3:9092")

  object Topics {
    val RandomNumbers = "random-numbers"
    val MultipliedRandomNumbers = "multiplied-random-numbers"
    val WikipediaChanges = "wikipedia-changes"
  }
}
