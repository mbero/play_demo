name := """my_first_app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "org.avaje.ebeanorm" % "avaje-ebeanorm" % "7.1.1"
libraryDependencies += "org.avaje" % "avaje-agentloader" % "2.1.2"
libraryDependencies += "org.avaje.ebeanorm" % "avaje-ebeanorm-agent" % "4.9.1"
libraryDependencies += "org.purl.wf4ever" % "rodl-client-common" % "2.9.1"


// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile)

lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
 
//Link to local maven repository 
resolvers += (
    "Local Maven Repository" at "file:///C:/Users/Marcin Berendt/.m2/repository"
    
)

fork in run := true