allprojects {
    group = "net.tegulis.template"
    version = "3.1.0"
}

// Kotlin plugin asks to be added here to prevent breaking the build
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

subprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}
