package me.rcmurphy.github.model

import me.rcmurphy.github.{UserAPI, GitHubAuth}
import me.rcmurphy.github.util.DateTimeReads
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(id: Long, login: String, name: Option[String], urls: UserURLs) extends RepositoryOwner

case class UserURLs(
  url: String,
  avatarUrl: String,
  eventsUrl: String,
  htmlUrl: String,
  followersUrl: String,
  followingUrl: String,
  gistsUrl: String,
  organizationsUrl: String,
  starredUrl: String,
  subscriptionsUrl: String,
  receivedEventsUrl: String,
  reposUrl: String
)

object User {
  import DateTimeReads._

  implicit val userUrlReads = (
    (__ \ "url").read[String] and
    (__ \ "avatar_url").read[String] and
    (__ \ "events_url").read[String] and
    (__ \ "html_url").read[String] and
    (__ \ "followers_url").read[String] and
    (__ \ "following_url").read[String] and
    (__ \ "gists_url").read[String] and
    (__ \ "organizations_url").read[String] and
    (__ \ "starred_url").read[String] and
    (__ \ "subscriptions_url").read[String] and
    (__ \ "received_events_url").read[String] and
    (__ \ "repos_url").read[String]
  )(UserURLs.apply _)

  implicit val userReads = (
    (__ \ "id").read[Long] and
    (__ \ "login").read[String] and
    (__ \ "name").readNullable[String] and
    (__).read[UserURLs]
  )(User.apply _)

}
