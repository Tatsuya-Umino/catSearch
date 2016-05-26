name := "NekoSearch"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaEbean,
  cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  filters,
  "eu.bitwalker" % "UserAgentUtils" % "1.19",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.apache.commons" % "commons-email" % "1.4"
)

libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"

play.Project.playJavaSettings
