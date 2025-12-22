package net.tegulis.template.app

import net.tegulis.template.Library
import net.tegulis.template.VERSION

fun main() {
    println("Main version: $VERSION")
    println(Library.generateMessage("world"))
}
