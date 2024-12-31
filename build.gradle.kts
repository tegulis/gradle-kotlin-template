import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.Path

plugins {
	application
	kotlin("jvm") version "2.0.21"
	id("com.gradleup.shadow") version "9.0.0-beta2"
}

group = "net.tegulis.template"
version = "1.1.0"

application {
	mainClass = "${project.group}.MainKt"
}

kotlin {
	jvmToolchain(21)
}

// Co-locate Java sources with Kotlin
sourceSets {
	main { java.srcDirs("src/main/kotlin") }
	test { java.srcDirs("src/test/kotlin") }
}

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	// TEST
	testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.10.3")
	testRuntimeOnly(group = "org.junit.platform", name = "junit-platform-launcher")
	// Google Truth
	testImplementation(group = "com.google.truth", name = "truth", version = "1.4.2")
}

tasks.withType<KotlinCompile> {
	dependsOn("replaceVersion")
}

tasks.register("replaceVersion") {
	group = "other"
	doFirst {
		val mainClassPath = application.mainClass.get()
			.replace(".", "/")
			.replace("Kt", ".kt")
		for (dir in sourceSets.main.get().kotlin.srcDirs) {
			val mainClassPathInSourceSet = "${dir}/${mainClassPath}"
			val mainClassFile = Path(mainClassPathInSourceSet).toFile()
			if (mainClassFile.exists()) {
				val contents = mainClassFile.readText()
				val modifiedContents = contents.replace(Regex("VERSION\\s+=\\s+\"(.*?)\""), "VERSION = \"${version}\"")
				mainClassFile.writeText(modifiedContents)
			}
		}
	}
}

tasks.withType<Test> {
	group = "verification"
	useJUnitPlatform()
	enableAssertions = true
	// Extra settings for very verbose testing
	testLogging {
		events = TestLogEvent.values().filter { it != TestLogEvent.STARTED }.toSet()
		exceptionFormat = TestExceptionFormat.FULL
		showExceptions = true
		showCauses = true
		showStackTraces = true
	}
	// Google Truth: don't clean stack traces
	systemProperty("com.google.common.truth.disable_stack_trace_cleaning", "true")
	// Don't generate reports
	reports.all { required = false }
}

tasks.named("shadowJar") {
	group = "distribution"
}

// Pack the shadowJar and asset files into a zip
tasks.register("packZip", Zip::class) {
	group = "distribution"
	dependsOn("shadowJar")
	archiveFileName = "${project.name}-${project.version}.zip"
	destinationDirectory = file("build/distributions")
	from("build/libs") {
		include("${project.name}-${project.version}-all.jar")
	}
	// Add assets here
	// from("assets") {
	// 	include("**/*")
	// }
}
