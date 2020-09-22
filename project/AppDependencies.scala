import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-backend-play-26" % "2.24.0",
    "uk.gov.hmrc" %% "domain" % "5.6.0-play-26",
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.30.0-play-26",
    "org.typelevel" %% "cats" % "0.9.0",
    "com.github.fge" % "json-schema-validator" % "2.2.6"
  )

  def test(scope: String = "test"): Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.9.0-play-26" % scope,
    "org.scalatest" %% "scalatest" % "3.0.8" % scope,
    "org.pegdown" % "pegdown" % "1.6.0" % scope,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % scope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % scope,
    "org.mockito" % "mockito-core" % "2.24.5" % scope
  )
}
