package me.rcmurphy.github.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Repository(id: Long, name: String, fullName: String, description: String, owner: RepositoryOwner, urls: RepositoryURLs) {

}

case class RepositoryURLs(
  url: String,
  htmlUrl: String,
  cloneUrl: String,
  gitUrl: String,
  sshUrl: String,
  svnUrl: String,
  mirrorUrl: Option[String]
)

object Repository {

  implicit val repositoryUrlsReads = (
    (__ \ "url").read[String] and
    (__ \ "html_url").read[String] and
    (__ \ "clone_url").read[String] and
    (__ \ "git_url").read[String] and
    (__ \ "ssh_url").read[String] and
    (__ \ "svn_url").read[String] and
    (__ \ "mirror_url").readNullable[String]
  )(RepositoryURLs.apply _)

  implicit val repositoryReads = (
    (__ \ "id").read[Long] and
    (__ \ "name").read[String] and
    (__ \ "full_name").read[String] and
    (__ \ "description").read[String] and
    (__ \ "owner").read[RepositoryOwner] and
    (__).read[RepositoryURLs]
  )(Repository.apply _)
}
/*
  {
    "id": 1296269,
    "owner": {
      "login": "octocat",
      "id": 1,
      "avatar_url": "https://github.com/images/error/octocat_happy.gif",
      "gravatar_id": "",
      "url": "https://api.github.com/users/octocat",
      "html_url": "https://github.com/octocat",
      "followers_url": "https://api.github.com/users/octocat/followers",
      "following_url": "https://api.github.com/users/octocat/following{/other_user}",
      "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
      "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
      "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
      "organizations_url": "https://api.github.com/users/octocat/orgs",
      "repos_url": "https://api.github.com/users/octocat/repos",
      "events_url": "https://api.github.com/users/octocat/events{/privacy}",
      "received_events_url": "https://api.github.com/users/octocat/received_events",
      "type": "User",
      "site_admin": false
    },
    "name": "Hello-World",
    "full_name": "octocat/Hello-World",
    "description": "This your first repo!",
    "private": false,
    "fork": true,
    "url": "https://api.github.com/repos/octocat/Hello-World",
    "html_url": "https://github.com/octocat/Hello-World",
    "clone_url": "https://github.com/octocat/Hello-World.git",
    "git_url": "git://github.com/octocat/Hello-World.git",
    "ssh_url": "git@github.com:octocat/Hello-World.git",
    "svn_url": "https://svn.github.com/octocat/Hello-World",
    "mirror_url": "git://git.example.com/octocat/Hello-World",
    "homepage": "https://github.com",
    "language": null,
    "forks_count": 9,
    "stargazers_count": 80,
    "watchers_count": 80,
    "size": 108,
    "default_branch": "master",
    "open_issues_count": 0,
    "has_issues": true,
    "has_wiki": true,
    "has_pages": false,
    "has_downloads": true,
    "pushed_at": "2011-01-26T19:06:43Z",
    "created_at": "2011-01-26T19:01:12Z",
    "updated_at": "2011-01-26T19:14:43Z",
    "permissions": {
      "admin": false,
      "push": false,
      "pull": true
    }
  }
 */