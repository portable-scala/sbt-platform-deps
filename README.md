# sbt-platform-deps

Provides the `%%%` dependency builder for sbt.
This is used by Scala Native and/or Scala.js projects, including cross-compiling projects, to select the appropriate dependencies for the platform.

This project is only intended as a common dependency for the sbt plugins of Scala.js and Scala Native.
It should not be directly used.

## Latest release

The latest release is 1.0.0-M1.
It can be depended on with

```scala
addSbtPlugin("org.portable-scala" % "sbt-platform-deps" % "1.0.0-M1")
```

As a user, you should typically never depend on sbt-platform-deps.
You will get it transitively through sbt-scalanative and/or sbt-scalajs.
