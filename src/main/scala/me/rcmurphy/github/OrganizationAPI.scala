package me.rcmurphy.github

import me.rcmurphy.github.model.User
import play.api.libs.ws.WSClient

trait OrganizationAPI {
  def getMembers(orgName: String): Stream[User]
}

class OrganizationAPIImpl()(implicit auth: GitHubAuth, override val ws: WSClient) extends OrganizationAPI with BaseAPI {
  override def getMembers(orgName: String): Stream[User] = {
    doGetList[User](baseUrl + s"orgs/$orgName/members")
  }
}

object OrganizationAPI extends BaseAPICompanion {
  def apply()(implicit auth: GitHubAuth): OrganizationAPI = {
    new OrganizationAPIImpl()
  }
}
