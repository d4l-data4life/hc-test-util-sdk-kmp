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

import care.data4life.sdk.util.test.config.TestConfig
import care.data4life.sdk.util.test.lang.FileNotFoundError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommonResourceLoaderTest {
    @Test
    fun `Given exists is called with a Path to a Fixture, returns false if the Fixture in Common does not exists`() {
        // Given
        val path = "/somthing.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).exists(path)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given exists is called with a Path to a Fixture and a RootPath, returns false if the Fixture in the given Path does not exists`() {
        // Given
        val path = "/somthingElse.json"
        val root = "/ROOT/"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).exists(path, root)

        // Then
        assertFalse(result)
    }

    @Test
    fun `Given exists is called with a Path to a Fixture, returns true if the Fixture in CommonTest exists`() {
        // Given
        val path = "/somethingElse.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).exists(path)

        // Then
        assertTrue(result)
    }

    @Test
    fun `Given exists is called with a Path to a Fixture and a RootPath, returns true if the Fixture in the given Path exists`() {
        // Given
        val root = "./src/jvmTest/resources/"
        val path = "/example.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).exists(path, root)

        // Then
        assertTrue(result)
    }

    // load

    @Test
    fun `Given load is called with a Path to a Fixture, throws an error if the Fixture in Common does not exists`() {
        // Given
        val path = "/somthing.json"

        // Then
        assertFailsWith<FileNotFoundError> {
            // When
            CommonResourceLoader(TestConfig.projectDir).load(path)
        }
    }

    @Test
    fun `Given load is called with a Path to a Fixture, returns the value of the File as String`() {
        // Given
        val path = "/somethingElse.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).load(path).trim()

        // Then
        assertEquals(
            actual = result,
            expected = "{ \"id\": \"你好\" }"
        )
    }

    @ExperimentalStdlibApi
    @Test
    fun `Given load is called with a Path and a ResourceEncoding, it uses the UTF8 by default`() {
        // Given
        val path = "/exampleEncoding.json"

        // When
        val result = CommonResourceLoader(
            TestConfig.projectDir
        ).load(path, encoding = ResourceEncoding.UTF_8).trim()

        // Then
        assertEquals(
            actual = result.substringAfter(':').trim('}').trim().trim('"')[0].code,
            expected = 92
        )
    }

    @ExperimentalStdlibApi
    @Test
    fun `Given load is called with a Path and a ResourceEncoding, it uses the ResourceEncoding`() {
        // Given
        val path = "/exampleEncoding.json"

        // When
        val result = CommonResourceLoader(
            TestConfig.projectDir
        ).load(path, encoding = ResourceEncoding.UTF_16).trim()

        // Then
        assertEquals(
            actual = result.substringAfter(':').trim('}').trim().trim('"')[0].code,
            expected = 31520
        )
    }

    @Test
    fun `Given load is called with a Path to a Fixture and a RootPath, throws an error if the Fixture in Common does not exists`() {
        // Given
        val path = "/somthingElse.json"
        val root = "/ROOT/"

        // Then
        assertFailsWith<FileNotFoundError> {
            // When
            CommonResourceLoader(TestConfig.projectDir).load(path, root = root)
        }
    }

    @Test
    fun `Given load is called with a Path to a Fixture and a RootPath, returns the value of the File as String`() {
        // Given
        val root = "./src/jvmTest/resources/"
        val path = "/example.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).load(path, root = root).trim()

        // Then
        assertEquals(
            actual = result,
            expected = "{ \"id\": \"example\" }"
        )
    }

    // loadBytes
    @Test
    fun `Given loadBytes is called with a Path to a Fixture, throws an error if the Fixture in Common does not exists`() {
        // Given
        val path = "/somthing.json"

        // Then
        assertFailsWith<FileNotFoundError> {
            // When
            CommonResourceLoader(TestConfig.projectDir).loadBytes(path)
        }
    }

    @Test
    fun `Given loadBytes is called with a Path to a Fixture, returns the value of the File as String`() {
        // Given
        val path = "/somethingElse.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).loadBytes(path)

        // Then
        assertTrue(
            result.contentEquals("{ \"id\": \"你好\" }\n".encodeToByteArray())
        )
    }

    @Test
    fun `Given loadBytes is called with a Path to a Fixture and a RootPath, throws an error if the Fixture in Common does not exists`() {
        // Given
        val path = "/somthingElse.json"
        val root = "/ROOT/"

        // Then
        assertFailsWith<FileNotFoundError> {
            // When
            CommonResourceLoader(TestConfig.projectDir).load(path, root = root)
        }
    }

    @Test
    fun `Given loadBytes is called with a Path to a Fixture and a RootPath, returns the value of the File as String`() {
        // Given
        val root = "./src/jvmTest/resources/"
        val path = "/example.json"

        // When
        val result = CommonResourceLoader(TestConfig.projectDir).loadBytes(path, root = root)

        // Then
        assertTrue(
            result.contentEquals("{ \"id\": \"example\" }\n".encodeToByteArray())
        )
    }
}
