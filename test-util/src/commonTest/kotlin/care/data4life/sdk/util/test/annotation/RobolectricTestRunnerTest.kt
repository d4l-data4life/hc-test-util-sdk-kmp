package care.data4life.sdk.util.test.annotation

import kotlin.test.Test
import kotlin.test.assertEquals

expect object TestBase64 {
    fun encode(data: ByteArray): String
}

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class RobolectricTestRunnerTest {
    @Test
    fun `Given a things runs dependend on Robolectric it works in common code`() {
        // Given
        val data = "The quick brown fox jumps over the lazy dog"

        // When
        val result = TestBase64.encode(data.encodeToByteArray())

        // Then
        assertEquals(
            expected = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZw==",
            actual = result,
        )
    }
}
