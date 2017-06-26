name := "monitor-dev"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.kamon" %% "kamon-core" % "0.6.6",
  "io.kamon" % "kamon-system-metrics_2.11" % "0.6.6",
  "io.kamon" % "kamon-influxdb_2.11" % "0.6.3",
  "io.kamon" % "kamon-log-reporter_2.11" % "0.6.6"
)

aspectjSettings
javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj
fork in run := true