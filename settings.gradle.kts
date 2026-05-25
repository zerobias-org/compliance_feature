pluginManagement {
    val localBuildTools = file("../util/packages/build-tools")
    if (localBuildTools.exists()) {
        includeBuild(localBuildTools)
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/zerobias-org/util")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: "zerobias-org"
                password = System.getenv("READ_TOKEN")
                    ?: System.getenv("NPM_TOKEN")
                    ?: System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("zb.workspace") version "1.+"
        id("zb.base") version "1.+"
        id("zb.content") version "1.+"
    }
}

rootProject.name = "compliance_features"

// Auto-discover compliance_feature packages under package/ —
// package/<vendor>/<code>/ (depth 2, e.g. package/zerobias/f_2fa/). The
// marker walk picks up build.gradle.kts at any depth. Project paths
// mirror the filesystem: package/zerobias/f_2fa → :zerobias:f_2fa.
//
// NOTE: the singleton complianceFeatureTypes/ artifact lives OUTSIDE
// package/ (different artifact type, compliance_feature_type) and is NOT
// migrated here — the reusable publish workflow only acts on package/**.
val packageDir = file("package")
if (packageDir.exists()) {
    packageDir.walkTopDown()
        .filter { it.name == "build.gradle.kts" }
        .forEach { buildFile ->
            val moduleDir = buildFile.parentFile
            val relativePath = moduleDir.relativeTo(packageDir).path
            val projectPath = relativePath.replace(File.separatorChar, ':')

            include(projectPath)
            project(":$projectPath").projectDir = moduleDir
        }
}
