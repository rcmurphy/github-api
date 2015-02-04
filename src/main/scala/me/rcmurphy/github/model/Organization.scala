package me.rcmurphy.github.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Organization(id: Long, login: String, url: String, avatarUrl: String, description: Option[String]) extends RepositoryOwner

object Organization {
  implicit val organizationReads = (
    (__ \ "id").read[Long] and
    (__ \ "login").read[String] and
    (__ \ "url").read[String] and
    (__ \ "avatar_url").read[String] and
    (__ \ "description").readNullable[String]
  )(Organization.apply _)
}
