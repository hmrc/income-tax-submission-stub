import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-backend-play-28"  % "5.7.0",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28" % "0.50.0",
    "com.github.fge" % "json-schema-validator" % "2.2.6"
  )

  def test(scope: String = "test,it"): Seq[ModuleID] = Seq(
    "uk.gov.hmrc"   %% "bootstrap-test-play-28"  % "5.7.0"  % scope,
    "org.scalatest" %% "scalatest" % "3.2.9" % scope,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % scope,
    "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % scope,
    "com.vladsch.flexmark" % "flexmark-all" % "0.36.8" % scope
  )
}
