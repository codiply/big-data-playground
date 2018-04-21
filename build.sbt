name := "Big Data Playground"

version := "0.0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.aerospike" % "aerospike-client" % "4.1.6",
  "com.lightbend" %% "kafka-streams-scala" % "0.2.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "0.18",
  "com.lightbend.akka" %% "akka-stream-alpakka-sse" % "0.18",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.20",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "io.spray" %%  "spray-json" % "1.3.3",
  "io.kamon" %% "kamon-core" % "1.1.2",
  "io.kamon" %% "kamon-statsd" % "0.6.7",
  "io.kamon" %% "kamon-system-metrics" % "1.0.0",
  "org.apache.kafka" % "kafka-clients" % "1.0.0"
)

assemblyOutputPath in assembly := file("target/big-data-playground/big-data-playground.jar")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => {
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
  }
}
