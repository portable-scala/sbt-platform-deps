val previousVersion = "1.0.2"

// Work around https://github.com/sbt/sbt/issues/6571
Global / excludeLintKeys += crossSbtVersions

inThisBuild(Seq(
  organization := "org.portable-scala",
  version := "1.0.3-SNAPSHOT",

  versionScheme := Some("semver-spec"),

  crossScalaVersions := Seq("2.12.3", "2.10.6"),
  scalaVersion := "2.12.3",
  scalacOptions ++= Seq("-deprecation", "-feature", "-encoding", "UTF-8"),

  crossSbtVersions := Seq("1.0.2", "0.13.16"),
  sbtVersion in pluginCrossBuild := "1.0.2",

  homepage := Some(url("https://github.com/portable-scala/sbt-platform-deps")),
  licenses += ("BSD 3-Clause",
      url("https://github.com/portable-scala/sbt-platform-deps/blob/master/LICENSE")),
  scmInfo := Some(ScmInfo(
      url("https://github.com/portable-scala/sbt-platform-deps"),
      "scm:git:git@github.com:portable-scala/sbt-platform-deps.git",
      Some("scm:git:git@github.com:portable-scala/sbt-platform-deps.git"))),
))

lazy val `sbt-platform-deps` = project.in(file(".")).
  enablePlugins(SbtPlugin).
  settings(
    scriptedLaunchOpts += "-Dplugin.version=" + version.value,
    scriptedBufferLog := false,

    // MiMa setup
    mimaPreviousArtifacts ++= {
      val dependency = organization.value % moduleName.value % previousVersion
      val sbtV = (pluginCrossBuild / sbtBinaryVersion).value
      val scalaV = (update / scalaBinaryVersion).value
      Set(Defaults.sbtPluginExtra(dependency, sbtV, scalaV))
    },

    // Publishing
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.endsWith("-SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := (
      <developers>
        <developer>
          <id>sjrd</id>
          <name>Sébastien Doeraene</name>
          <url>https://github.com/sjrd/</url>
        </developer>
        <developer>
          <id>densh</id>
          <name>Denys Shabalin</name>
          <url>https://github.com/densh/</url>
        </developer>
      </developers>
    ),
    pomIncludeRepository := { _ => false },
  )
