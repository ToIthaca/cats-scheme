name := "cats-scheme"

version := "1.0"

scalaVersion := "2.11.8"

name := "cats-playground"
version := "1.0"

lazy val buildSettings = Seq(
  organization := "com.ithaca",
  scalaVersion := "2.11.7"
)

lazy val testSettings = Seq(
  libraryDependencies ++= Seq(
    "junit" % "junit" % "4.12" % "test"
  )
)

lazy val commonSettings = Seq(
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1"),
  scalacOptions ++= commonScalacOptions,
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats" % "0.5.0"
  )
) ++ compilerOptions ++ testSettings

lazy val commonScalacOptions = Seq(
  "-encoding", "UTF-8",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros"
)

lazy val compilerOptions = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation"
  )
)

lazy val root = (project in file("."))
  .settings(buildSettings, commonSettings)

    