package me.rcmurphy.github

import me.rcmurphy.github.model.{Repository, Organization, User}
import play.api.libs.ws.WSClient

import scala.concurrent.Future

trait UserAPI {

  def get(login: String): Future[User]
  def getCurrent: Future[User]
}

class UserAPIImpl()(implicit auth: GitHubAuth, override val ws: WSClient) extends UserAPI with BaseAPI {

  override def get(login: String): Future[User] = {
    doGet[User](baseUrl + s"users/$login")
  }
  override def getCurrent: Future[User] = {
    doGet[User](baseUrl + "user")
  }
}

object UserAPI extends BaseAPI with BaseAPICompanion {
  def apply()(implicit auth: GitHubAuth): UserAPI = {
    new UserAPIImpl()
  }

  protected[github] def getOrganizations(user: User)(implicit auth: GitHubAuth): Stream[Organization] = {
    doGetList[Organization](user.urls.organizationsUrl)
  }
  protected[github] def getRepositories(user: User, repoType: String)(implicit auth: GitHubAuth): Stream[Repository] = {
    doGetList[Repository](user.urls.reposUrl, "type" -> repoType)
  }
}
