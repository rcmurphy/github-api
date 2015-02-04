package me.rcmurphy.github

import play.api.libs.ws.{WSAuthScheme, WSRequestHolder}

sealed trait GitHubAuth {
  protected[github] def addAuthToRequest(req: WSRequestHolder): WSRequestHolder
}

case class NoopGitHubAuth() extends GitHubAuth {
  override protected[github] def addAuthToRequest(req: WSRequestHolder): WSRequestHolder = {
    req
  }
}

case class BasicGitHubAuth(
  username: String,
  password: String
) extends GitHubAuth {
  override protected[github] def addAuthToRequest(req: WSRequestHolder): WSRequestHolder = {
    req.withAuth(username, password, WSAuthScheme.BASIC)
  }
}

case class OAuthGitHubAuth(
  token: String
) extends GitHubAuth {

  override protected[github] def addAuthToRequest(req: WSRequestHolder): WSRequestHolder = {
    req.withHeaders("Authorization" -> s"token $token")
  }
}
