package care.data4life.sdk.util.test.annotation

import care.data4life.sdk.util.objc.NSDataMapper
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import platform.CoreCrypto.CC_SHA1
import platform.CoreCrypto.CC_SHA1_DIGEST_LENGTH
import platform.Foundation.NSString
import platform.Foundation.stringByAppendingFormat
import platform.posix.uint8_tVar

actual object TestHash {
    actual fun hash(data: ByteArray): String {
        val nsData = NSDataMapper.toNSData(data)
        val bytes = memScoped {
            val digest = allocArray<uint8_tVar>(CC_SHA1_DIGEST_LENGTH)

            CC_SHA1(
                nsData.bytes,
                nsData.length.toUInt(),
                digest
            ) ?: throw RuntimeException("")

            return@memScoped digest.readBytes(CC_SHA1_DIGEST_LENGTH)
        }

        val sha1String = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            sha1String.append(("" as NSString).stringByAppendingFormat("%02hhx", byte))
        }

        return sha1String.toString()
    }
}
