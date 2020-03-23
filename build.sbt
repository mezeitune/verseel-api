enablePlugins(JavaServerAppPackaging)

name := "verseel-api"

version := "0.1"

scalaVersion := "2.12.6"

organization := "com.verseel"

libraryDependencies ++= {
  val akkaVersion = "2.5.12"
  val akkaHttp = "10.1.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core"  % akkaHttp,
    "com.typesafe.akka" %% "akka-http"       % akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.2.3",
    "de.heikoseeberger" %% "akka-http-play-json"   % "1.17.0",
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test",
    "org.scalatest"     %% "scalatest"       % "3.0.5"       % "test",
    "com.github.scullxbones" %% "akka-persistence-mongo-scala" % "2.3.2",
    "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.14" % "test",
    "com.typesafe.akka"         %% "akka-persistence"  % akkaVersion,
    "org.iq80.leveldb"           % "leveldb"           % "0.7",
    "org.fusesource.leveldbjni"  % "leveldbjni-all"    % "1.8"
  )
}
