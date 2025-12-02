package net.tegulis.template.app

import net.tegulis.template.Library

const val VERSION = "2.0.0"

fun main() {
    println("Main version: $VERSION")
    println(Library.generateMessage("world"))
}
