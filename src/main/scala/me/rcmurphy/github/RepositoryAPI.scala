package me.rcmurphy.github

import me.rcmurphy.github.model.Repository
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait RepositoryAPI {

  def get(owner: String, repository: String): Future[Repository]
  def getCurrentUserRepositories: Stream[Repository]
}

class RepositoryAPIImpl()(implicit auth: GitHubAuth, override val ws: WSClient) extends RepositoryAPI with BaseAPI {

  override def get(owner: String, repository: String): Future[Repository] = {
    val response = doGet[Repository](baseUrl + s"repos/$owner/$repository")
    response.recover {
      case exc: Exception =>
        logger.error("Exception getting repository.", exc)
        throw exc
    }
  }

  override def getCurrentUserRepositories: Stream[Repository] = {
    doGetList[Repository](baseUrl + "user/repos")
  }

}

object RepositoryAPI extends BaseAPICompanion {
  def apply()(implicit auth: GitHubAuth): RepositoryAPI = {
    new RepositoryAPIImpl()
  }
}
