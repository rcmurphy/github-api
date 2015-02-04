package me.rcmurphy.github.model

import enumeratum.EnumEntry.Lowercase
import enumeratum._
import play.api.libs.json._

sealed trait PullRequestState extends EnumEntry with Lowercase

object PullRequestState extends Enum[PullRequestState] {

  val values = findValues

  implicit val reads = new Reads[PullRequestState] {
    def reads(json: JsValue): JsResult[PullRequestState] = json match {
      case JsString(s) => {
        try {
          JsSuccess(withName(s))
        } catch {
          case _: NoSuchElementException => JsError(s"Enumeration expected of type: '$getClass', but it does not appear to contain the value: '$s'")
        }
      }
      case _ => JsError("String value expected")
    }
  }
  case object Closed extends PullRequestState
  case object Open extends PullRequestState

}
