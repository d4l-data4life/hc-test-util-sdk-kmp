package care.data4life.sdk.util.test.annotation

actual object PlatformRunner {
    actual fun androidOnly(): String {
        throw RuntimeException()
    }

    actual fun jvmOnly(): String {
        return "test"
    }

    actual fun iOSOnly(): String {
        throw RuntimeException()
    }
}
