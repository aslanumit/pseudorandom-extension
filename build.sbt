import org.nlogo.build.NetLogoExtension

enablePlugins(NetLogoExtension)

version    := "1.1.1"
isSnapshot := true

scalaVersion          := "2.12.12"
Compile / scalaSource := baseDirectory.value / "src" / "main"
Test / scalaSource    := baseDirectory.value / "src" / "test"
scalacOptions        ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii", "-release", "11")

netLogoExtName      := "pseudorandom"
netLogoClassManager := "org.nlogo.extensions.pseudorandom.PseudoRandomExtension"
netLogoVersion      := "6.3.0"
//netLogoZipExtras   ++= Seq(baseDirectory.value / "README.md", baseDirectory.value / "example-models")
netLogoZipExtras   ++= Seq(baseDirectory.value / "README.md")

// Current version of Spire, 0.18, does not work with Scala 2.12
//  so we have to use 0.17
libraryDependencies += "org.typelevel" %% "spire" % "0.17.0"