/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.sdk.util.test

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

internal object NSDataMapper {
    // see: https://github.com/JetBrains/kotlin-native/issues/3172
    // see: https://stackoverflow.com/questions/58521108/how-to-convert-kotlin-bytearray-to-nsdata-and-viceversa
    // see: https://gist.github.com/noahsark769/61cfb7a8b7231e2069a9dab94cf74a62
    fun toByteArray(data: NSData): ByteArray {
        return ByteArray(data.length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), data.bytes, data.length)
            }
        }
    }

    @ExperimentalUnsignedTypes
    fun toNSData(data: ByteArray): NSData {
        return memScoped {
            NSData.create(
                bytes = allocArrayOf(data),
                length = data.size.toULong()
            )
        }
    }
}
