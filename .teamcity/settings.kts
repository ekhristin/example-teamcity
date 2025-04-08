import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.03"

project {

    buildType(Build)

    template(id656)
}

object Build : BuildType({
    templates(id656)
    name = "Build"

    artifactRules = """
        target/original-plaindoll-0.0.7.jar
        target/plaindoll-0.0.7.jar
    """.trimIndent()
    publishArtifacts = PublishMode.SUCCESSFUL
})

object id656 : Template({
    id("656")
    name = "656"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            name = "test"
            id = "Maven2"

            conditions {
                doesNotContain("teamcity.build.branch", "master")
            }
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            userSettingsSelection = "settings.xml"
        }
        maven {
            name = "Deploy"
            id = "Maven2_1"

            conditions {
                contains("teamcity.build.branch", "master")
            }
            goals = "clean deploy"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            userSettingsSelection = "settings.xml"
        }
    }

    triggers {
        vcs {
            id = "TRIGGER_1"
        }
    }

    features {
        perfmon {
            id = "perfmon"
        }
    }
})
