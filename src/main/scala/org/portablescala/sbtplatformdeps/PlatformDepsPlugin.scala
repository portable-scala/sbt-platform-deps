package org.portablescala.sbtplatformdeps

import scala.language.implicitConversions

import sbt._
import sbt.Keys._

object PlatformDepsPlugin extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    /** `CrossVersion` used for `%%%` dependencies (read only in most cases).
     *
     *  Usually, you should never set `platformDepsCrossVersion` yourself. It
     *  is the responsibility of the sbt plugin providing a particular platform
     *  to do so (e.g., `ScalaJSPlugin` or `ScalaNativePlugin`).
     *
     *  This setting is read by `%%%`.
     */
    val platformDepsCrossVersion: SettingKey[CrossVersion] = {
      SettingKey[CrossVersion]("platformDepsCrossVersion",
          "CrossVersion used for %%% dependencies", sbt.KeyRanks.DSetting)
    }

    implicit def toPlatformDepsGroupID(groupID: String): PlatformDepsGroupID = {
      require(groupID.nonEmpty, "Group ID may not be empty")
      new PlatformDepsGroupID(groupID)
    }
  }

  import autoImport._

  override def globalSettings: Seq[Setting[_]] = Def.settings(
      // By default, %%% uses JVM dependencies
      platformDepsCrossVersion := CrossVersion.binary
  )
}
