import java.io.ByteArrayOutputStream

tasks.register("createGitHubRelease") {
    doLast {
        val version = project.version.toString()
        val githubToken = System.getenv("PUBLISH_TOKEN")

        if (githubToken.isNullOrBlank()) {
            throw GradleException("PUBLISH_TOKEN environment variable is not set")
        }

        val json = """
            {
              "tag_name": "v$version",
              "target_commitish": "master",
              "name": "v$version",
              "body": "Version $version",
              "draft": false,
              "prerelease": false
            }
        """.trimIndent()

        exec {
            commandLine(
                "curl", "-X", "POST", "-H", "Authorization: token $githubToken", "-d", json,
                "https://api.github.com/repos/rebecaalbuquerque/android-fake-server/releases"
            )
        }
    }
}
