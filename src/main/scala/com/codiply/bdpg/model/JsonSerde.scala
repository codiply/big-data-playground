package com.codiply.bdpg.model

import java.nio.charset.StandardCharsets
import java.util

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import spray.json._

class JsonSerializer[T](implicit format: JsonFormat[T]) extends Serializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def serialize(topic: String, data: T): Array[Byte] = data.toJson.toString.getBytes(StandardCharsets.UTF_8)

  override def close(): Unit = ()
}

class JsonDeserializer[T >: Null <: Any : Manifest](implicit format: JsonFormat[T]) extends Deserializer[T] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def deserialize(topic: String, data: Array[Byte]): T = {
    if (data == null) {
      return null
    } else {
      new String(data, StandardCharsets.UTF_8).parseJson.convertTo[T]
    }
  }
}

class JsonSerde[T >: Null <: Any : Manifest](implicit format: JsonFormat[T]) extends Serde[T] {
  override def deserializer(): Deserializer[T] = new JsonDeserializer[T]

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = ()

  override def close(): Unit = ()

  override def serializer(): Serializer[T] = new JsonSerializer[T]
}
