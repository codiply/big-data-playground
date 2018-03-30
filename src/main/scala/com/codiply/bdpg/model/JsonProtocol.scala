package com.codiply.bdpg.model

import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit val wikipediaChangeFormat = jsonFormat6(WikipediaChange)
}
