package com.codiply.bdpg.model

import org.apache.kafka.common.serialization.{Serde, Serdes, StringDeserializer, StringSerializer}
import org.apache.kafka.common.serialization.Serdes.{IntegerSerde, StringSerde}
import org.apache.kafka.streams.kstream.Windowed
import org.apache.kafka.streams.kstream.internals.{WindowedDeserializer, WindowedSerializer}

object AllSerdes {
  import AllSerializers._
  import AllDeserializers._
  import com.codiply.bdpg.model.JsonProtocol._

  lazy val integerSerde: Serde[Integer] = new IntegerSerde
  lazy val stringSerde: Serde[String] = new StringSerde
  lazy val wikipediaChangeSerde: Serde[WikipediaChange] = new JsonSerde[WikipediaChange]

  lazy val windowedStringSerde: Serde[Windowed[String]] =
    Serdes.serdeFrom(windowedStringSerializer, windowedStringDeserializer)
}

object AllSerializers {
  lazy val stringSerializer = new StringSerializer()

  lazy val windowedStringSerializer = new WindowedSerializer[String](stringSerializer)
}

object AllDeserializers {
  lazy val stringDeserializer = new StringDeserializer()

  lazy val windowedStringDeserializer = new WindowedDeserializer[String](stringDeserializer)
}
