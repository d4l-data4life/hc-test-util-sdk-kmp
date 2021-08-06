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

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    // Publish
    id("care.data4life.sdk.util.test.publishing-config")
}

group = care.data4life.sdk.util.test.LibraryConfig.group

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }

    jvm()

    ios {
        binaries {
            framework {
                baseName = care.data4life.sdk.util.test.LibraryConfig.name
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibCommon)
            }
        }
        val commonTest by getting {
            kotlin.srcDir("src-gen/commonTest/kotlin")

            dependencies {
                implementation(Dependencies.multiplatform.kotlin.testCommon)
                implementation(Dependencies.multiplatform.kotlin.testCommonAnnotations)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.testJvm)
                implementation(Dependencies.multiplatform.kotlin.testJvmJunit)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibJdk8)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.testJvm)
                implementation(Dependencies.multiplatform.kotlin.testJvmJunit)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibNative)
                implementation(Dependencies.multiplatform.d4l.sdkUtil)
            }
        }

        val iosTest by getting {
            dependencies {
                dependencies {
                    implementation(Dependencies.multiplatform.kotlin.testCommon)
                    implementation(Dependencies.multiplatform.kotlin.testCommonAnnotations)
                }
            }
        }
    }
}

android {
    compileSdkVersion(care.data4life.sdk.util.test.LibraryConfig.android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(care.data4life.sdk.util.test.LibraryConfig.android.minSdkVersion)
        targetSdkVersion(care.data4life.sdk.util.test.LibraryConfig.android.targetSdkVersion)

        versionCode = 1
        versionName = "${project.version}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(
            mapOf(
                "clearPackageData" to "true"
            )
        )
    }

    resourcePrefix(care.data4life.sdk.util.test.LibraryConfig.android.resourcePrefix)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.setSrcDirs(setOf("src/androidMain/kotlin"))
            res.setSrcDirs(setOf("src/androidMain/res"))
        }

        getByName("test") {
            java.setSrcDirs(setOf("src/androidTest/kotlin"))
            res.setSrcDirs(setOf("src/androidTest/res"))
        }
    }
}

val templatesPath = "${projectDir}/src/commonTest/resources/template"
val configPath = "${projectDir}/src-gen/commonTest/kotlin/care/data4life/sdk/util/test/config"

val provideTestConfig: Task by tasks.creating {
    doFirst {
        val templates = File(templatesPath)
        val configs = File(configPath)

        val config = File(templates, "TestConfig.tmpl")
            .readText()
            .replace("PROJECT_DIR", projectDir.toPath().toAbsolutePath().toString())

        if (!configs.exists()) {
            if(!configs.mkdir()) {
                System.err.println("The script not able to create the config directory")
            }
        }
        File(configPath, "TestConfig.kt").writeText(config)
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinCompile::class.java) {
    if (this.name.contains("Test")) {
        this.dependsOn(provideTestConfig)
    }
}
