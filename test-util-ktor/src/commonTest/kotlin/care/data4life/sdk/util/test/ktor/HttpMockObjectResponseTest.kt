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

package care.data4life.sdk.util.test.ktor

import care.data4life.sdk.util.test.coroutine.runBlockingTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.get
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class HttpMockObjectResponseTest {
    @Test
    fun `It fulfils HttpClientFeature`() {
        val feature: Any = HttpMockObjectResponse

        assertTrue(feature is HttpClientFeature<*, *>)
    }

    @Test
    fun `It has a key`() {
        assertEquals(
            actual = HttpMockObjectResponse.key.name,
            expected = "HttpMockObjectResponse"
        )
    }

    @Test
    fun `Given a response had been set up it overwrites the response which is not Any with the given one`() = runBlockingTest {
        // Given
        val objectResponse = Pair("That's", "it!")
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respondOk("Never mind")
                }
            }

            install(HttpMockObjectResponse) {
                addResponse(objectResponse)
            }
        }

        // When
        val result = client.get<Pair<String, String>>("somewhere")

        // Then
        assertSame(
            actual = result,
            expected = objectResponse
        )
    }

    @Test
    fun `Given a response had been set up it overwrites the response which is not Any with the given one for an arbitrary number of calls`() = runBlockingTest {
        // Given
        val objectResponse = Pair("That's", "it!")
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respondOk("Never mind")
                }
            }

            install(HttpMockObjectResponse) {
                addResponse(objectResponse)
            }
        }

        // When
        client.get<Pair<String, String>>("somewhere")
        client.get<Pair<String, String>>("somewhere else")
        val result = client.get<Pair<String, String>>("anywhere")

        // Then
        assertSame(
            actual = result,
            expected = objectResponse
        )
    }

    @Test
    fun `Given multiple responses set up it overwrites the responses which is not Any with the given ones`() = runBlockingTest {
        // Given
        val objectResponses = listOf(
            Pair("That's", "it!"),
            Pair("That's", "it, too!"),
            Pair("That's", "the one"),
        )
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respondOk("Never mind")
                }
            }

            install(HttpMockObjectResponse) {
                addResponses(objectResponses)
            }
        }

        for (objectResponse in objectResponses) {
            // When
            val result = client.get<Pair<String, String>>("somewhere")

            // Then
            assertSame(
                actual = result,
                expected = objectResponse
            )
        }
    }

    @Test
    fun `Given a response had been installed and set up it overwrites the responses which is not Any with the given ones and returns the latest response for an arbitrary number of calls`() = runBlockingTest {
        // Given
        val objectResponses = listOf(
            Pair("That's", "it!"),
            Pair("That's", "it, too!"),
            Pair("That's", "the one"),
        )
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respondOk("Never mind")
                }
            }

            install(HttpMockObjectResponse) {
                addResponses(objectResponses)
            }
        }

        // When
        for (objectResponse in objectResponses) {
            client.get<Pair<String, String>>("somewhere")
        }

        client.get<Pair<String, String>>("somewhere")
        client.get<Pair<String, String>>("somewhere else")
        val result = client.get<Pair<String, String>>("anywhere")

        // Then
        assertSame(
            actual = result,
            expected = objectResponses.last()
        )
    }
}
