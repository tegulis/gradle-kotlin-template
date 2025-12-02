package net.tegulis.gradle

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class ReplaceVersionTest {
    @Test
    fun testReplacement() {
        val scenarios = mapOf(
            Pair(
                "var VERSION = \"1.2.3\"",
                "var VERSION = \"4.5.6\"",
            ),
            Pair(
                "var VERSION = \"1.2.3-alpha+build\"",
                "var VERSION = \"4.5.6\"",
            ),
            Pair(
                "const val VERSION = \"1.2.3\"",
                "const val VERSION = \"4.5.6\"",
            ),
            Pair(
                "const val VERSION = \"1.2.3-alpha+build\"",
                "const val VERSION = \"4.5.6\"",
            ),
            Pair(
                "const val VERSION = Foobar(\"1.2.3\")",
                "const val VERSION = Foobar(\"4.5.6\")",
            ),
            Pair(
                "const val VERSION = Foobar(\"1.2.3-alpha+build\")",
                "const val VERSION = Foobar(\"4.5.6\")",
            ),
        )
        val newVersion = "4.5.6"
        scenarios.forEach { (input, expected) -> Truth.assertThat(replaceVersion(input, newVersion)).isEqualTo(expected) }
    }
}
