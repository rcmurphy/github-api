package me.rcmurphy.github.model

import me.rcmurphy.github.{GitHubAuth, PullRequestAPI}
import me.rcmurphy.github.util.DateTimeReads
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.Future

case class PullRequest(
  id: Long,
  title: String,
  state: PullRequestState,
  number: Int,
  user: User,
  body: Option[String],
  createdAt: DateTime,
  updatedAt: DateTime,
  closedAt: Option[DateTime],
  mergedAt: Option[DateTime],
  head: Ref,
  base: Ref,
  urls: PullRequestURLs
) {
  def getReviewComments()(implicit auth: GitHubAuth): Future[Seq[Comment]] = {
    PullRequestAPI.getComments(urls.commentsUrl)
  }

  def toShortString: String = {
    (
      Seq(
        id,
        title,
        state,
        number,
        user.login,
        "createdAt=" + createdAt,
        "updatedAt=" + updatedAt
      ) ++
        closedAt.map(c => "closedAt=" + c) ++
        mergedAt.map(m => "mergedAt=" + m)
    ).mkString("PullRequest(", ",", ")")
  }
}

object PullRequest {
  import DateTimeReads._
  import User._

  implicit val urlReads = (
    (__ \ "html_url").read[String] and
    (__ \ "diff_url").read[String] and
    (__ \ "patch_url").read[String] and
    (__ \ "issue_url").read[String] and
    (__ \ "commits_url").read[String] and
    (__ \ "review_comments_url").read[String] and
    (__ \ "review_comment_url").read[String] and
    (__ \ "comments_url").read[String] and
    (__ \ "statuses_url").read[String]
  )(PullRequestURLs.apply _)

  implicit val reads = (
    (__ \ "id").read[Long] and
    (__ \ "title").read[String] and
    (__ \ "state").read[PullRequestState] and
    (__ \ "number").read[Int] and
    (__ \ "user").read[User] and
    (__ \ "body").readNullable[String] and
    (__ \ "created_at").read[DateTime] and
    (__ \ "updated_at").read[DateTime] and
    (__ \ "closed_at").readNullable[DateTime] and
    (__ \ "merged_at").readNullable[DateTime] and
    (__ \ "head").read[Ref] and
    (__ \ "base").read[Ref] and
    (__).read[PullRequestURLs]
  )(PullRequest.apply _)
}

case class PullRequestURLs(
  htmlUrl: String,
  diffUrl: String,
  patchUrl: String,
  issueUrl: String,
  commitsUrl: String,
  reviewCommentsUrl: String,
  reviewCommentUrl: String,
  commentsUrl: String,
  statusesUrl: String
)
