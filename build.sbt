name := "scala-collections-benchmark"

scalaVersion := "2.12.1"

libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.8.2"

parallelExecution in Test := true // turn on in order to get more precise benchmark results

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")