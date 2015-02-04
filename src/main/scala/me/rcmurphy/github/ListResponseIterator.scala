package me.rcmurphy.github

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import play.api.libs.json.Reads
import play.api.libs.ws.{WSRequestHolder, WSClient}
import scala.collection.mutable

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class ListResponseIterator[T](val initialReq: WSRequestHolder)(implicit reads: Reads[T], auth: GitHubAuth, ws: WSClient) extends Iterator[T] {

  val logger = Logger(LoggerFactory.getLogger(getClass))

  var nextReq: Option[WSRequestHolder] = Some(initialReq)

  val resultQueue = mutable.Queue[T]()

  override def hasNext: Boolean = {
    if (resultQueue.nonEmpty) {
      true
    } else {
      if (nextReq.isDefined) {
        parseResults(nextReq.get)
        resultQueue.nonEmpty
      } else {
        false
      }
    }
  }

  def parseResults(targetReq: WSRequestHolder): Unit = {
    val future = targetReq.get().map { res =>
      BaseAPI.logLimits(res)
      val nextUrl = BaseAPI.getLinkToNext(res)
      nextReq = nextUrl map { n =>
        ws.url(n) withGHAuth ()
      }
      val content = res.json.validate[Seq[T]].getOrThrow
      logger.debug(s"Enqueuing ${content.length} results" + nextUrl.map(url => s" [nextUrl = '$nextUrl']").getOrElse(""))
      resultQueue.enqueue(content: _*)
    }
    Await.result(future, 5.seconds)
  }

  override def next(): T = {
    if (resultQueue.nonEmpty) {
      resultQueue.dequeue()
    } else if (nextReq.isDefined) {
      parseResults(nextReq.get)
      resultQueue.dequeue()
    } else {
      throw new Exception("No more elements")
    }
  }
}

object ListResponseIterator {

  def apply[T](initialUrl: String, queryParams: (String, String)*)(implicit reads: Reads[T], auth: GitHubAuth, ws: WSClient): Iterator[T] = {
    val req = ws.url(initialUrl).withQueryString(queryParams: _*).withGHAuth()
    apply(req)
  }
  def apply[T](initialReq: WSRequestHolder)(implicit reads: Reads[T], auth: GitHubAuth, ws: WSClient): Iterator[T] = {
    new ListResponseIterator[T](initialReq)
  }
}
