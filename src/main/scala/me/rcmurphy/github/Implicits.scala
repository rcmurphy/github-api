package me.rcmurphy.github

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsError, JsSuccess, JsResult}
import play.api.libs.ws.WSRequestHolder

trait Implicits {
  protected val logger = Logger(LoggerFactory.getLogger(getClass))

  implicit class RichReq(inner: WSRequestHolder) {
    def withGHAuth()(implicit auth: GitHubAuth): WSRequestHolder = auth.addAuthToRequest(inner)
  }

  implicit class RichJsResult[T](inner: JsResult[T]) {
    def getOrThrow: T = {
      inner match {
        case JsSuccess(pr, _) =>
          pr
        case JsError(error) =>
          logger.error(s"Error parsing JSON: $error")
          throw new Exception(s"Error parsing JSON: $error")
      }
    }
  }
}
