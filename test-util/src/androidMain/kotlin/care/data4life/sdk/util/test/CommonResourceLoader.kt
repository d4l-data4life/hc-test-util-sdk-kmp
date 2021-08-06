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

import care.data4life.sdk.util.test.lang.FileNotFoundError
import java.io.File
import java.nio.charset.Charset

actual class CommonResourceLoader actual constructor(
    projectDir: AbsolutePath
) {
    private val projectPath = projectDir

    actual fun exists(path: Path, root: Path?): Boolean {
        val resource = File(
            CommonPathResolver.resolvePath(
                projectPath,
                root,
                path
            )
        )

        return resource.exists()
    }

    @Throws(FileNotFoundError::class)
    private fun resolveFile(path: Path, root: Path?): File {
        return if (!exists(path, root)) {
            throw FileNotFoundError()
        } else {
            File(
                CommonPathResolver.resolvePath(
                    projectPath,
                    root,
                    path
                )
            )
        }
    }

    private fun mapEncoding(encoding: ResourceEncoding): Charset {
        return if (encoding == ResourceEncoding.UTF_16) {
            Charsets.UTF_16
        } else {
            Charsets.UTF_8
        }
    }

    @Throws(FileNotFoundError::class)
    actual fun load(
        path: Path,
        encoding: ResourceEncoding,
        root: Path?
    ): String {
        return resolveFile(path, root).readText(mapEncoding(encoding))
    }

    @Throws(FileNotFoundError::class)
    actual fun loadBytes(path: Path, root: Path?): ByteArray {
        return resolveFile(path, root).readBytes()
    }
}
