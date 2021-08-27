package care.data4life.sdk.util.test.annotation

import java.security.MessageDigest

actual object TestHash {
    actual fun hash(data: ByteArray): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(data)

        val hexResult = StringBuilder(bytes.size * 2)
        for (byte in bytes)
            hexResult.append("%02x".format(byte))

        return hexResult.toString()
    }
}
