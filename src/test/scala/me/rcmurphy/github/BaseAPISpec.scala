package me.rcmurphy.github

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.io.Source

trait BaseAPISpec {

  val logger = Logger(LoggerFactory.getLogger(getClass))

  val config = ConfigFactory.load("api-test")

  val apiKey = config.getString("api-key")

  val testTargetAccount = config.getString("test-target.account")
  val testTargetRepo = config.getString("test-target.repo")

  protected def getTestData(resourceName: String) = {
    Source.fromInputStream(getClass.getResourceAsStream(resourceName)).mkString
  }

}
