package me.rcmurphy.github

import me.rcmurphy.github.model.{Comment, PullRequest}
import me.rcmurphy.github.model.PullRequest.reads
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success

trait PullRequestAPI {
  def getAll(owner: String, repo: String): Stream[PullRequest]
  def getOpen(owner: String, repo: String): Stream[PullRequest]
  def getByBranch(owner: String, repo: String, baseBranch: String, sortBy: String = "created", state: String = "open"): Stream[PullRequest]
}

class PullRequestAPIImpl(implicit val auth: GitHubAuth, override val ws: WSClient) extends PullRequestAPI with BaseAPI {
  override def getAll(owner: String, repo: String): Stream[PullRequest] = {
    doGetList[PullRequest](repoUrl(owner, repo) + "pulls", "state" -> "all")
  }
  override def getOpen(owner: String, repo: String): Stream[PullRequest] = {
    doGetList[PullRequest](repoUrl(owner, repo) + "pulls")
  }
  override def getByBranch(owner: String, repo: String, baseBranch: String, sortBy: String = "created", state: String = "open"): Stream[PullRequest] = {
    require(state == "open" || state == "closed" || state == "all")
    require(sortBy == "created" || sortBy == "updated" || sortBy == "popularity" || sortBy == "long-running")
    doGetList[PullRequest](repoUrl(owner, repo) + s"pulls", "base" -> baseBranch, "sort" -> sortBy, "state" -> state, "direction" -> "desc")
  }
}

object PullRequestAPI extends BaseAPI with BaseAPICompanion {

  def apply()(implicit auth: GitHubAuth): PullRequestAPI = {
    new PullRequestAPIImpl()
  }

  protected[github] def getComments(url: String)(implicit auth: GitHubAuth): Future[Seq[Comment]] = {
    val comments = doGet[Seq[Comment]](url)
    comments.onComplete {
      case Success(result) => logger.debug(s"Retrieved Comment list [length=${result.length}]")
      case _ =>
    }
    comments
  }

}
