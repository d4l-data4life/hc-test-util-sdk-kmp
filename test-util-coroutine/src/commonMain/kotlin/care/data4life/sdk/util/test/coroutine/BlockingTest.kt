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
import kotlin.coroutines.CoroutineContext

// see: https://github.com/Kotlin/kotlinx.coroutines/issues/1996
expect val testCoroutineContext: CoroutineContext
expect fun runBlockingTest(block: suspend CoroutineScope.() -> Unit)
// Please note: this solves a specific iOS with shared immutable states while running in async Context
expect fun runWithContextBlockingTest(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> Unit
)
