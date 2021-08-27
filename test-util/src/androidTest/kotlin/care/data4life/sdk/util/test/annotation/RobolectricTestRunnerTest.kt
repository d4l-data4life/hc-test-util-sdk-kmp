package care.data4life.sdk.util.test.annotation

actual object TestBase64 {
    actual fun encode(data: ByteArray): String {
        return android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP)
    }
}
