package org.portablescala.sbtplatformdeps

import sbt._

import scala.language.experimental.macros
import scala.reflect.macros.Context

final class PlatformDepsGroupID private[sbtplatformdeps] (
    private val groupID: String) {
  def %%%(artifactID: String): PlatformDepsGroupArtifactID =
    macro PlatformDepsGroupID.cross_binary_impl
}

object PlatformDepsGroupID {
  /** Internal, used by the macro implementing [[PlatformDepsGroupID.%%%]].
   *
   *  Use [[PlatformDepsPlugin.autoImport.platformDepsCrossVersion]] instead.
   */
  val platformDepsCrossVersion: SettingKey[CrossVersion] =
    PlatformDepsPlugin.autoImport.platformDepsCrossVersion

  /** Internal, used by the macro implementing [[PlatformDepsGroupID.%%%]].
   *
   *  Use:
   *  {{{
   *  ("a" % artifactID % revision).cross(cross)
   *  }}}
   *  instead.
   */
  def withCross(groupID: PlatformDepsGroupID, artifactID: String,
      cross: CrossVersion): PlatformDepsGroupArtifactID = {
    require(artifactID.nonEmpty, "Artifact ID must not be empty")
    new PlatformDepsGroupArtifactID(groupID.groupID, artifactID, cross)
  }

  def cross_binary_impl(c: Context { type PrefixType = PlatformDepsGroupID })(
      artifactID: c.Expr[String]): c.Expr[PlatformDepsGroupArtifactID] = {
    import c.universe._
    reify {
      PlatformDepsGroupID.withCross(c.prefix.splice, artifactID.splice,
          PlatformDepsGroupID.platformDepsCrossVersion.value)
    }
  }
}

final class PlatformDepsGroupArtifactID private[sbtplatformdeps] (
    groupID: String, artifactID: String, crossVersion: CrossVersion) {
  def %(revision: String): ModuleID = {
    require(revision.nonEmpty, "Revision must not be empty")
    ModuleID(groupID, artifactID, revision).cross(crossVersion)
  }
}
