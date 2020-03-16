import scala.sys.process.Process

name := "ChutneyConfigServer"

version := "0.1"

scalaVersion := "2.12.8"

// typesafe config
libraryDependencies += "com.typesafe" % "config" % "1.4.0"

// Zookeeper Connection
libraryDependencies += "org.apache.curator" % "curator-framework" % "4.2.0" exclude("org.apache.zookeeper", "zookeeper")
libraryDependencies += "org.apache.curator" % "curator-x-discovery" % "4.2.0" exclude("org.apache.zookeeper", "zookeeper")

libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.13"


libraryDependencies += "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion
// Why below fails
//libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"


PB.targets in Compile := Seq(scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value)
//PB.protoSources in Compile ++= Seq(file("../../../../src/proto/configServer/configserver.proto"))
//PB.includePaths in Compile := Seq(file("../../../../src/proto/configServer"))


PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)
