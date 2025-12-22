package net.tegulis.template

object Library {
    // Fancy code to depend on kotlin-stdlib
    val versionText = VERSION.let { "Library version: $it" }

    fun generateMessage(name: String) = "Hello $name!\n$versionText"
}
