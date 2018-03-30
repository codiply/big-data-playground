name := "Big Data Playground"

version := "0.0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.lightbend" %% "kafka-streams-scala" % "0.2.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-sse" % "0.18",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "io.spray" %%  "spray-json" % "1.3.3",
  "org.apache.kafka" % "kafka-clients" % "1.0.0"
)

assemblyOutputPath in assembly := file("target/big-data-playground/big-data-playground.jar")
