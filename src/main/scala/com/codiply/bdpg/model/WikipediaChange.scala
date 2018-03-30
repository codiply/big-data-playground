package com.codiply.bdpg.model

case class WikipediaChange(
    bot: Boolean,
    id: Option[Long],
    server_name: String,
    timestamp: Option[Long],
    title: String,
    user: String)
