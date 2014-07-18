import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

  val appName         = "anormDemo1"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.github.play2war.ext" %% "redirect-playlogger" % "1.0.1" 

  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
     Play2WarPlugin.play2WarSettings: _* 
  ).settings(
    // Add your own project settings here    
        Play2WarKeys.servletVersion := "3.0"
  )

}
