name := "sans"
version := "0.1.2-SNAPSHOT"
scalaVersion := "2.12.4"

enablePlugins(SbtReactiveAppPlugin)

TaskKey[Unit]("check") := {
  val outputDir = (stage in Docker).value
  val contents = IO.readLines(outputDir / "Dockerfile")
  val lines = Seq(
    """LABEL com.lightbend.rp.namespace="sans""""
  )

  lines.foreach { line =>
    if (!contents.contains(line)) {
      sys.error(
        s"""Dockerfile is missing line "$line" - Dockerfile contents:
           |${contents.mkString("\n")}
         """.stripMargin)
    }
  }

  val dockerUsernameValue = (dockerUsername in Docker).value
  val dockerUsernameValueExpected = Some("sans")
  assert(dockerUsernameValue == dockerUsernameValueExpected,
    s"Docker repository value is $dockerUsernameValue - expected $dockerUsernameValueExpected}")

  val namespaceValue = namespace.value
  val namespaceValueExpected = Some("sans")
  assert(namespaceValue  == namespaceValueExpected,
    s"Namespace value is $namespaceValue - expected $namespaceValueExpected}")
}