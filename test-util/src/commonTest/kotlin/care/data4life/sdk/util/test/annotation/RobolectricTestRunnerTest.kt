package care.data4life.sdk.util.test.annotation

import kotlin.test.Test
import kotlin.test.assertEquals

expect object TestHash {
    fun hash(data: ByteArray): String
}

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class RobolectricTestRunnerTest {
    @Test
    fun `Given a things runs dependend on Robolectric, it works in common code`() {
        // Given
        val data = "The quick brown fox jumps over the lazy dog"

        // When
        val result = TestHash.hash(data.encodeToByteArray())

        // Then
        assertEquals(
            "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12",
            result,
            "Failed to create sha1"
        )
    }
}
