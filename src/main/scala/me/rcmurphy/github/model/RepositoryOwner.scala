package me.rcmurphy.github.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

trait RepositoryOwner {
  val id: Long
  val login: String
}

object RepositoryOwner {

  implicit val organizationReads = new Reads[RepositoryOwner] {
    override def reads(json: JsValue): JsResult[RepositoryOwner] = {
      (json \ "type").as[String] match {
        case "User" =>
          User.userReads.reads(json)
        case "Organization" =>
          Organization.organizationReads.reads(json)
        case x =>
          throw new Exception("Unhandled owner type: $x")

      }
    }
  }
}
