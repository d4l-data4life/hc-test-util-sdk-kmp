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

import care.data4life.sdk.util.NSDataMapper
import care.data4life.sdk.util.test.lang.FileNotFoundError
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF16StringEncoding
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.posix.F_OK
import platform.posix.access

actual class CommonResourceLoader actual constructor(
    projectDir: AbsolutePath
) {
    private val projectPath = projectDir

    actual fun exists(path: Path, root: Path?): Boolean {
        return access(
            CommonPathResolver.resolvePath(
                projectPath,
                root,
                path
            ),
            F_OK
        ) == 0
    }

    private fun readBytes(path: String): NSData {
        return memScoped {
            NSFileManager.defaultManager().contentsAtPath(
                path
            ) ?: RuntimeException("Out of Memory")
        } as NSData
    }

    private fun mapEncoding(encoding: ResourceEncoding): ULong {
        return if (encoding == ResourceEncoding.UTF_16) {
            NSUTF16StringEncoding
        } else {
            NSUTF8StringEncoding
        }
    }

    @Throws(FileNotFoundError::class)
    actual fun load(
        path: Path,
        encoding: ResourceEncoding,
        root: Path?
    ): String {
        return if (!exists(path, root)) {
            throw FileNotFoundError()
        } else {
            memScoped {
                NSString.create(
                    readBytes(
                        CommonPathResolver.resolvePath(
                            projectPath,
                            root,
                            path
                        )
                    ),
                    mapEncoding(encoding)
                )
            }
        } as String
    }

    @Throws(FileNotFoundError::class)
    actual fun loadBytes(path: Path, root: Path?): ByteArray {
        return if (!exists(path, root)) {
            throw FileNotFoundError()
        } else {
            NSDataMapper.toByteArray(
                readBytes(
                    CommonPathResolver.resolvePath(
                        projectPath,
                        root,
                        path
                    )
                )
            )
        }
    }
}
