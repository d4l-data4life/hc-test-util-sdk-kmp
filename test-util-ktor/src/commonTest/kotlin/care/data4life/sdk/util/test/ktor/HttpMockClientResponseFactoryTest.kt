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
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.Test
import kotlin.test.assertEquals

class HttpMockClientResponseFactoryTest {
    @Test
    fun `Given createHelloWorldOkResponse is called, it creates OkResponse, which contains Hello World`() = runBlockingTest {
        // Given
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    HttpMockClientResponseFactory.createHelloWorldOkResponse(this)
                }
            }
        }

        // When
        val responsePayload = client.get<String>("potato")
        val response = (client.engine as MockEngine).responseHistory[0]

        // Then
        assertEquals(
            actual = responsePayload,
            expected = "Hello World!"
        )
        assertEquals(
            actual = response.statusCode,
            expected = HttpStatusCode.OK
        )
        assertEquals(
            actual = response.headers,
            expected = headersOf(
                "Content-Type" to listOf(
                    ContentType.Text.Plain.toString()
                )
            )
        )
    }
}
