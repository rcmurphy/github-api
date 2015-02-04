package me.rcmurphy.github

import com.typesafe.scalalogging.Logger
import org.joda.time.{DateTimeZone, DateTime}
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.libs.ws.{WSResponse, WSClient}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait BaseAPI {
  import BaseAPI._

  val logger = Logger(LoggerFactory.getLogger(getClass))

  //val accepts = "application/vnd.github.v3+json"
  val accepts = "application/vnd.github.moondragon+json"
  val baseUrl = "https://api.github.com/"

  protected implicit val ws: WSClient

  def repoUrl(owner: String, repo: String): String = {
    baseUrl + s"repos/$owner/$repo/"
  }

  def doGet[T](targetUrl: String, queryParams: (String, String)*)(implicit reads: Reads[T], auth: GitHubAuth): Future[T] = {
    val req = ws.url(targetUrl).withQueryString(queryParams: _*).withGHAuth()
    req.get().map {
      res =>
        logLimits(res)
        res.json.validate[T].getOrThrow
    }
  }

  def doGetList[T](targetUrl: String, queryParams: (String, String)*)(implicit reads: Reads[T], auth: GitHubAuth): Stream[T] = {
    ListResponseIterator[T](targetUrl, queryParams: _*).toStream
  }
}

protected[github] object BaseAPI {
  protected val logger = Logger(LoggerFactory.getLogger(getClass))

  val linkNextRegex = """.*<(.*)>; rel="next".*""".r
  val linkLastRegex = """.*<(.*)>; rel="last".*"""

  def logLimits(response: WSResponse): Unit = {
    for {
      limit <- response.header("X-RateLimit-Limit")
      remaining <- response.header("X-RateLimit-Remaining")
      reset <- response.header("X-RateLimit-Reset").map(r => new DateTime(1000L * r.toLong, DateTimeZone.UTC))
    } {
      logger.debug(s"$remaining / $limit requests left. Reset at $reset")
    }
  }

  def getLinkToNext(response: WSResponse): Option[String] = {
    response.header("Link") collect {
      case linkNextRegex(next) => next
    }
  }
}

trait BaseAPICompanion {
  private val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
  protected implicit val ws: WSClient = new play.api.libs.ws.ning.NingWSClient(builder.build())
}
