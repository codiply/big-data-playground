package com.codiply.bdpg.constants

case class CassandraFamily(keyspace: String, table: String)

object CassandraCluster {
  val HOSTS: List[String] = List("cassandra-1", "cassandra-2", "cassandra-3")
  val PORT: Int = 9042

  object Keyspaces {
    val Wikipedia: String = "wikipedia"
  }

  object Families {
    val wikipediaChanges: CassandraFamily = CassandraFamily(Keyspaces.Wikipedia, "changes")
  }
}
