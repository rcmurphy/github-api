package me.rcmurphy.github.model

import me.rcmurphy.github.util.DateTimeReads
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Comment(
  id: Long,
  user: User,
  body: String,
  createdAt: DateTime,
  updatedAt: DateTime
) {
  /*
    {
     "url": "https://api.github.com/repos/octocat/Hello-World/pulls/comments/1",
     "id": 1,
     "diff_hunk": "@@ -16,33 +16,40 @@ public class Connection : IConnection...",
     "path": "file1.txt",
     "position": 1,
     "original_position": 4,
     "commit_id": "6dcb09b5b57875f334f61aebed695e2e4193db5e",
     "original_commit_id": "9c48853fa3dc5c1c3d6f1f1cd1f2743e72652840",
     "user": {

     },
     "body": "Great stuff",
     "created_at": "2011-04-14T16:00:49Z",
     "updated_at": "2011-04-14T16:00:49Z",
     "html_url": "https://github.com/octocat/Hello-World/pull/1#discussion-diff-1",
     "pull_request_url": "https://api.github.com/repos/octocat/Hello-World/pulls/1",
     "_links": {
       "self": {
         "href": "https://api.github.com/repos/octocat/Hello-World/pulls/comments/1"
       },
       "html": {
         "href": "https://github.com/octocat/Hello-World/pull/1#discussion-diff-1"
       },
       "pull_request": {
         "href": "https://api.github.com/repos/octocat/Hello-World/pulls/1"
       }
     }
   }

   */
}

object Comment {
  import DateTimeReads._
  import User._

  implicit val reads = (
    (__ \ "id").read[Long] and
    (__ \ "user").read[User] and
    (__ \ "body").read[String] and
    (__ \ "created_at").read[DateTime] and
    (__ \ "updated_at").read[DateTime]
  )(Comment.apply _)
}
