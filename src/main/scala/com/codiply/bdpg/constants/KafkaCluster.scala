package com.codiply.bdpg.constants

object KafkaCluster {
  val BROKERS: List[String] = List("kafka-1:9092", "kafka-2:9092", "kafka-3:9092")
  val ZOOKEEPER_NODES: List[String] = List("zookeeper-1:2181", "zookeeper-2:2181", "zookeeper-3:2181")

  object Topics {
    val RandomNumbers = "random-numbers"
    val MultipliedRandomNumbers = "multiplied-random-numbers"
    val WikipediaChanges = "wikipedia-changes"
    val WikipediaChangesStatistics = "wikipedia-changes-statistics"
  }
}
