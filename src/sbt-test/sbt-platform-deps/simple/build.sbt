val testDependency = settingKey[ModuleID]("Test ModuleID that we test")
val check = taskKey[Unit]("Check that the testDependency was correctly constructed")

val TestOrg = "org.example"
val TestArtifact = "foobar"
val TestVersion = "1.2.3"

def assertEquals(expected: Any, actual: Any): Unit =
  assert(actual == expected, s"Expected: $expected ; actual: $actual")

inThisBuild(Seq(
  version := "0.1",
  scalaVersion := "2.12.3"
))

lazy val `jvm-by-default` = project.in(file("jvm-by-default")).
  settings(
    testDependency := TestOrg %%% TestArtifact % TestVersion,

    check := {
      val moduleID = testDependency.value
      assertEquals(TestOrg, moduleID.organization)
      assertEquals(TestArtifact, moduleID.name)
      assertEquals(TestVersion, moduleID.revision)

      // In sbt 0.13, CrossVersion's do not have a meaningful ==
      if (sbtVersion.value.startsWith("0.13."))
        assertEquals("Binary", moduleID.crossVersion.toString)
      else
        assertEquals(CrossVersion.binary, moduleID.crossVersion)
    }
  )

lazy val `custom-cross-version` = project.in(file("custom-cross-version")).
  settings(
    platformDepsCrossVersion := CrossVersion.full,
    testDependency := TestOrg %%% TestArtifact % TestVersion,

    check := {
      val moduleID = testDependency.value
      assertEquals(TestOrg, moduleID.organization)
      assertEquals(TestArtifact, moduleID.name)
      assertEquals(TestVersion, moduleID.revision)

      // In sbt 0.13, CrossVersion's do not have a meaningful ==
      if (sbtVersion.value.startsWith("0.13."))
        assertEquals("Full", moduleID.crossVersion.toString)
      else
        assertEquals(CrossVersion.full, moduleID.crossVersion)
    }
  )
