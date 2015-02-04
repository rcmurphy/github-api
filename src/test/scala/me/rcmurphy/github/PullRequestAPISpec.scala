package me.rcmurphy.github

import java.io.{InputStreamReader, InputStream}
import java.nio.ByteBuffer
import java.util

import com.typesafe.scalalogging.Logger
import play.api.mvc.Results._
import play.api.test.Helpers._
import mockws.MockWS
import org.scalatest.{BeforeAndAfter, Matchers, FlatSpec}
import org.slf4j.LoggerFactory
import play.api.mvc.Action
import scala.concurrent.duration._
import scala.concurrent.Await

class PullRequestAPISpec extends FlatSpec with BaseAPISpec with BeforeAndAfter with Matchers {

  implicit val auth = OAuthGitHubAuth(apiKey)

  var api: PullRequestAPI = _

  implicit val ws = MockWS {
    case (GET, "https://api.github.com/repos/testacct/testrepo/pulls") =>
      Action { Ok(getTestData("/test-data/pr-list.json")) }
  }

  "PullRequestAPISpec" should "list all PRs" in {
    api = new PullRequestAPIImpl()

    val result = api.getOpen("testacct", "testrepo")

    result.length should equal(1)
  }
}
