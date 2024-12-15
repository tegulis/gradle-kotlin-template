package net.tegulis.template

const val VERSION = "1.1.0"

fun main() {
	println("Main version $VERSION")
	println(Library.generateMessage("world"))
}
