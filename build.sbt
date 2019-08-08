name := "Uber blacklist"

version := "0.1"

scalaVersion := "2.12.8"

val scalaTestVersion  = "3.0.5"
val akkaHttpVersion   = "10.1.5"
val akkaVersion       = "2.5.16"
val firebaseVersion   = "3.0.1"

enablePlugins(JavaAppPackaging)

mainClass in Compile := Some("UBApp")

libraryDependencies ++= Seq(
  "com.typesafe.akka"     %%      "akka-actor"            % akkaVersion,
  "com.typesafe.akka"     %%      "akka-stream"           % akkaVersion,
  "com.typesafe.akka"     %%      "akka-slf4j"            % akkaVersion,
  "com.typesafe.akka"     %%      "akka-http-spray-json"  % akkaHttpVersion,
  "org.scalatest"         %%      "scalatest"             % scalaTestVersion  % Test,
  "com.typesafe.akka"     %%       "akka-testkit"         % akkaVersion       % Test,
  "org.scalatest"         %%       "scalatest"            % scalaTestVersion  % Test,
  "com.typesafe.akka"     %%   "akka-http-testkit"        % akkaHttpVersion   % Test,
  "com.google.firebase"   %    "firebase-server-sdk"      % firebaseVersion,
  "mysql"                 %   "mysql-connector-java"      % "5.1.24"
)