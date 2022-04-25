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

import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.create
import kotlin.test.Test
import kotlin.test.assertTrue

class NSDataMapperTest {
    @ExperimentalUnsignedTypes
    @Test
    fun `Given a toNSData is called with ByteArray converts to NSData`() {
        // Given
        val payload = "ABC".encodeToByteArray()

        // When
        val result: Any = NSDataMapper.toNSData(payload)

        // Then
        assertTrue(result is NSData)
    }

    @Test
    fun `Given a toByteArray is called with NSData converts to ByteArray`() {
        // Given
        val payload = memScoped {
            NSData.create(
                base64EncodedString = "U3RyaW5nIHRvIGVuY29kZQ==",
                options = 0
            ) ?: throw NullPointerException("Out of memory.")
        }

        // When
        val result: Any = NSDataMapper.toByteArray(payload)

        // Then
        assertTrue(result is ByteArray)
    }

    @ExperimentalUnsignedTypes
    @Test
    fun `Given a toNSData is called with a ByteArray and the result calls toByteArray it transforms it to is orginal state`() {
        // Given
        val payload = "ABC".encodeToByteArray()

        // When
        val nsData = NSDataMapper.toNSData(payload)
        val byteArray = NSDataMapper.toByteArray(nsData)

        // Then
        assertTrue(payload.contentEquals(byteArray))
    }
}
