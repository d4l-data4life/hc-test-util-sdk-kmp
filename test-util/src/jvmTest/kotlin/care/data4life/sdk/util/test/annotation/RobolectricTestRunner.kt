package care.data4life.sdk.util.test.annotation

actual object TestBase64 {
    actual fun encode(data: ByteArray): String {
        return java.util.Base64.getEncoder().encodeToString(data)
    }
}
