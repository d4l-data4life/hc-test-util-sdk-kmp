package care.data4life.sdk.util.test.annotation

import care.data4life.sdk.util.test.NSDataMapper
import platform.Foundation.base64EncodedStringWithOptions

actual object TestBase64 {
    actual fun encode(data: ByteArray): String {
        val nsData = NSDataMapper.toNSData(data)
        return nsData.base64EncodedStringWithOptions(0)
    }
}
