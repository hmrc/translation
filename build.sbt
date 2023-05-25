import Dependencies._

lazy val root = (project in file(".")).
  settings(
    name := "welsh-translation-tool",
    inThisBuild(List(
      organization := "uk.gov.hmrc",
      scalaVersion := "2.12.15",
      version      := "0.2.0"
    )),
    libraryDependencies += scalaTest % Test,
    libraryDependencies += swing % Compile
  )

Compile / mainClass := Some("translate.ConvertMessages")
run / fork := true
