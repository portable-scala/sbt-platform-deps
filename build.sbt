val previousVersion = "1.0.0"

inThisBuild(Seq(
  organization := "org.portable-scala",
  version := "1.0.1-SNAPSHOT",

  scalaVersion := "2.12.3",
  scalacOptions ++= Seq("-deprecation", "-feature", "-encoding", "UTF-8"),

  crossSbtVersions := Seq("1.0.2", "0.13.16"),

  homepage := Some(url("https://github.com/portable-scala/sbt-platform-deps")),
  licenses += ("BSD 3-Clause",
      url("https://github.com/portable-scala/sbt-platform-deps/blob/master/LICENSE")),
  scmInfo := Some(ScmInfo(
      url("https://github.com/portable-scala/sbt-platform-deps"),
      "scm:git:git@github.com:portable-scala/sbt-platform-deps.git",
      Some("scm:git:git@github.com:portable-scala/sbt-platform-deps.git"))),
))

lazy val `sbt-platform-deps` = project.in(file(".")).
  settings(
    sbtPlugin := true,

    scriptedLaunchOpts += "-Dplugin.version=" + version.value,
    scriptedBufferLog := false,

    // MiMa setup
    mimaPreviousArtifacts ++= {
      val dependency = organization.value % moduleName.value % previousVersion
      val sbtV = (sbtBinaryVersion in pluginCrossBuild).value
      val scalaV = (scalaBinaryVersion in update).value
      Set(Defaults.sbtPluginExtra(dependency, sbtV, scalaV))
    },

    // Publish to Bintray, without the sbt-bintray plugin
    publishMavenStyle := false,
    publishTo := {
      val proj = moduleName.value
      val ver = version.value
      if (isSnapshot.value) {
        None // Bintray does not support snapshots
      } else {
        val url = new java.net.URL(
            s"https://api.bintray.com/content/portable-scala/sbt-plugins/$proj/$ver")
        val patterns = Resolver.ivyStylePatterns
        Some(Resolver.url("bintray", url)(patterns))
      }
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
