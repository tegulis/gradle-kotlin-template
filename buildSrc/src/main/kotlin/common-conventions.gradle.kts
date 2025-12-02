//import net.tegulis.gradle.ReplaceVersion
import com.ncorti.ktfmt.gradle.TrailingCommaManagementStrategy
import net.tegulis.gradle.ReplaceVersion
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.ncorti.ktfmt.gradle")
    id("com.github.ben-manes.versions")
}

group = "net.tegulis.template"
version = "2.0.0"

kotlin.jvmToolchain(24)

// Co-locate Java sources with Kotlin
sourceSets {
    main { java.srcDirs("src/main/kotlin") }
    test { java.srcDirs("src/test/kotlin") }
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

// Until https://github.com/gradle/gradle/issues/15383#issuecomment-779893192, we have this:
val libs: VersionCatalog = versionCatalogs.named("libs")
fun fromCatalog(alias: String) = libs.findLibrary(alias).get()

dependencies {
    // Kotlin
    //implementation(fromCatalog("kotlin-stdlib-jdk8"))
    // TESTING
    // JUnit Jupiter
    testImplementation(fromCatalog("junit-jupiter"))
    testRuntimeOnly(fromCatalog("junit-platform"))
    // Google Truth
    testImplementation(fromCatalog("google-truth"))
}

ktfmt {
    kotlinLangStyle()
    maxWidth.set(120)
    removeUnusedImports.set(true)
    trailingCommaManagementStrategy.set(TrailingCommaManagementStrategy.COMPLETE)
}

tasks.register<ReplaceVersion>("replaceVersion")

// Don't check formatting unless explicitly asked for
listOf("ktfmtCheck", "ktfmtCheckMain", "ktfmtCheckScripts", "ktfmtCheckTest").forEach { taskName ->
    tasks.named(taskName) {
        enabled = gradle.startParameter.taskNames.contains(this.name)
    }
}

// Replace version and format code before compiling
tasks.withType<KotlinCompile> {
    dependsOn("replaceVersion")
    dependsOn("ktfmtFormat")
}

tasks.withType<Test> {
    // Check formatting before testing for good measure, even though it will be skipped since it is disabled below
    dependsOn("ktfmtCheck")
    useJUnitPlatform()
    enableAssertions = true
    // Extra settings for very verbose testing
    testLogging {
        events = TestLogEvent.entries.filter { it != TestLogEvent.STARTED }.toSet()
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
    // Google Truth: don't clean stack traces
    systemProperty("com.google.common.truth.disable_stack_trace_cleaning", "true")
    // Don't generate reports
    reports.all { required = false }
    // Temporary solution for MockK: https://github.com/mockk/mockk/issues/1171
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}
