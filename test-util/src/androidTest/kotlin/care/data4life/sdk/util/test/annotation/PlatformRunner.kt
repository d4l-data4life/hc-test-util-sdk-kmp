package care.data4life.sdk.util.test.annotation

actual object PlatformRunner {
    actual fun androidOnly(): String {
        return "test"
    }

    actual fun jvmOnly(): String {
        throw RuntimeException()
    }

    actual fun iOSOnly(): String {
        throw RuntimeException()
    }
}
