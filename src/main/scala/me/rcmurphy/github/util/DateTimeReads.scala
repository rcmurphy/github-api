package me.rcmurphy.github.util

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.data.validation.ValidationError
import play.api.libs.json._

object DateTimeReads {
  val fmt = ISODateTimeFormat.dateTimeNoMillis()
  implicit val isoDateTimeReads: Reads[DateTime] = new Reads[DateTime] {
    override def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsString(value) => JsSuccess(fmt parseDateTime value)
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("validate.error.expected.datetime"))))
    }
  }
}
