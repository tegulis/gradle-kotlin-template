package net.tegulis.template

const val VERSION = "2.0.0"

object Library {
    // Fancy code to depend on kotlin-stdlib
    val version = VERSION.let { "Library version: $it" }

    fun generateMessage(name: String) = "Hello $name!\n$version"
}
