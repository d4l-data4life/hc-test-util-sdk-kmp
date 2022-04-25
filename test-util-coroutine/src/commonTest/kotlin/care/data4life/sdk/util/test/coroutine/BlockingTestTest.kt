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

package care.data4life.sdk.util.test.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.test.Test
import kotlin.test.assertEquals

class BlockingTestTest {
    @Test
    fun `Given runBlocking is executed and contains CoroutineScope it runs the given Scope`() = runBlockingTest {
        // Given
        val sample = "Hello CoroutineScope"
        val channel = Channel<String>()

        // When
        launch {
            channel.send(sample)
        }

        // Then
        assertEquals(
            actual = channel.receive(),
            expected = sample
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `Given runWithContextBlocking test is executed and contains CoroutineScope it runs the given Scope`() = runWithContextBlockingTest(GlobalScope.coroutineContext) {
        // Given
        val sample = "Hello CoroutineScope"
        val channel = Channel<String>()

        // When
        launch {
            CoroutineScope(testCoroutineContext).launch {
                channel.send(sample)
            }
        }

        // Then
        assertEquals(
            actual = channel.receive(),
            expected = sample
        )
    }
}
