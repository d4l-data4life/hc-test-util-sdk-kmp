package care.data4life.sdk.util.test.annotation

import kotlin.test.Test
import kotlin.test.assertEquals

class PlatformOnlyTest {
    @Test
    @AndroidOnly
    fun `Given it is AndroidOnly annotated it runs only on Android`() {
        assertEquals(
            actual = PlatformRunner.androidOnly(),
            expected = "test"
        )
    }

    @Test
    @JvmOnly
    fun `Given it is JvmOnly annotated it runs only on Jvm`() {
        assertEquals(
            actual = PlatformRunner.jvmOnly(),
            expected = "test"
        )
    }

    @Test
    @iOSOnly
    fun `Given it is iOSOnly annotated it runs only on iOS`() {
        assertEquals(
            actual = PlatformRunner.iOSOnly(),
            expected = "test"
        )
    }
}

expect object PlatformRunner {
    fun androidOnly(): String
    fun jvmOnly(): String
    fun iOSOnly(): String
}
