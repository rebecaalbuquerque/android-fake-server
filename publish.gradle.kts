import java.io.ByteArrayOutputStream

tasks.register("gitTag") {
    doLast {
        val tagName = project.version.toString()
        val result = ByteArrayOutputStream()
        exec {
            commandLine("git", "tag", "--list", tagName)
            standardOutput = result
        }
        val tagExists = result.toString().trim().isNotEmpty()
        if (tagExists) {
            throw GradleException("This version already exists, update the library version and try publishing again.")
        } else {
            exec {
                commandLine("git", "tag", "-a", tagName, "-m", tagName)
            }
            exec {
                commandLine("git", "push", "origin", tagName)
            }
        }
    }
}

tasks.register("createGitHubRelease") {
    doLast {
        val tagName = "v${project.version}"
        val githubToken = System.getenv("GITHUB_TOKEN")

        if (githubToken.isNullOrBlank()) {
            throw GradleException("GITHUB_TOKEN environment variable is not set")
        }

        val description = project.findProperty("releaseDescription") as String?
            ?: throw GradleException("Please provide a release description as a project property using -PreleaseDescription=\"description\"")

        val json = """
            {
              "tag_name": "$tagName",
              "target_commitish": "master",
              "name": "$tagName",
              "body": "$description",
              "draft": false,
              "prerelease": false
            }
        """.trimIndent()

        exec {
            commandLine("curl", "-X", "POST", "-H", "Authorization: token $githubToken", "-d", json,
                "https://api.github.com/repos/rebecaalbuquerque/android-fake-server/releases")
        }
    }
}

tasks.register("publishFakeServerVersion") {
    dependsOn("gitTag", "createGitHubRelease")
}
