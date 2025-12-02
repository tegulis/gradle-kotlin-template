plugins {
    id("common-conventions")
    id("application")
    id("com.gradleup.shadow")
}

application.mainClass = "${project.group}.${project.name}.MainKt"

dependencies {
    implementation(project(":lib"))
}

tasks["shadowJar"].group = "distribution"

listOf("startScripts", "distZip", "distTar", "shadowDistZip", "shadowDistTar").forEach { taskName ->
    tasks.named(taskName) {
        group = "disabled"
        enabled = false
    }
}

// Pack the shadowJar and asset files into a zip
tasks.register("packZip", Zip::class) {
    group = "distribution"
    dependsOn("shadowJar")
    archiveFileName = "${project.name}-${project.version}.zip"
    destinationDirectory = file("build/distributions")
    from("build/libs") { include("${project.name}-${project.version}-all.jar") }
    // Add assets here
    // from("assets") {
    // 	include("**/*")
    // }
}
