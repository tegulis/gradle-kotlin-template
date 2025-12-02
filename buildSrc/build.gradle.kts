plugins { `kotlin-dsl` }

repositories { gradlePluginPortal() }

dependencies {
    // Gradle plugins
    implementation(libs.kotlin.plugin)
    implementation(libs.gradle.shadow.plugin)
    implementation(libs.gradle.ktfmt.plugin)
    implementation(libs.gradle.versions.plugin)
    // Regular build dependencies
    // Testing the buildSrc code
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
    testImplementation(libs.google.truth)
}

tasks.withType<Test> {
    useJUnitPlatform()
    enableAssertions = true
}
