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

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.AttributeKey
import kotlin.native.concurrent.ThreadLocal

class HttpMockObjectResponse(
    private val responses: List<Any>
) {
    init {
        Counter.internalCounter = 0
    }

    val currentResponse: Any
        get() = responses[Counter.internalCounter]

    private fun isLast(): Boolean {
        return Counter.internalCounter == responses.lastIndex
    }

    fun next() {
        if (!isLast()) {
            Counter.internalCounter++
        }
    }

    @ThreadLocal
    private object Counter {
        var internalCounter = 0
    }

    class Config {
        internal val responses: MutableList<Any> = mutableListOf()

        fun addResponse(response: Any) {
            responses.add(response)
        }

        fun addResponses(responses: List<Any>) {
            this.responses.addAll(responses)
        }
    }

    companion object Feature : HttpClientFeature<Config, HttpMockObjectResponse> {
        override val key: AttributeKey<HttpMockObjectResponse> = AttributeKey("HttpMockObjectResponse")

        override fun prepare(block: Config.() -> Unit): HttpMockObjectResponse {
            val config = Config().apply(block)

            with(config) {
                return HttpMockObjectResponse(responses)
            }
        }

        override fun install(feature: HttpMockObjectResponse, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.After) { (info, _) ->
                val value = feature.currentResponse
                feature.next()

                proceedWith(
                    HttpResponseContainer(
                        info,
                        value
                    )
                )
            }
        }
    }
}
