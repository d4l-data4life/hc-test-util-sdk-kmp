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

import care.data4life.gradle.util.test.dependency.Dependency
import care.data4life.gradle.util.test.config.LibraryConfig

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    // Publish
    id("care.data4life.gradle.util.test.script.publishing-config")
}

group = LibraryConfig.group

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }

    jvm()

    ios {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibCommon)
            }
        }
        val commonTest by getting {
            kotlin.srcDir("src-gen/commonTest/kotlin")

            dependencies {
                implementation(Dependency.multiplatform.kotlin.testCommon)
                implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibAndroid)
                implementation(Dependency.androidTest.robolectric)

                implementation(Dependency.multiplatform.kotlin.testJvmJunit)
            }
        }

        val androidAndroidTestRelease by getting
        val androidTestFixtures by getting
        val androidTestFixturesDebug by getting
        val androidTestFixturesRelease by getting

        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependsOn(androidTestFixtures)
            dependsOn(androidTestFixturesDebug)
            dependsOn(androidTestFixturesRelease)
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testJvm)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibJdk8)

                implementation(Dependency.multiplatform.kotlin.testJvmJunit)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testJvm)
                implementation(Dependency.multiplatform.kotlin.testJvmJunit)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibNative)

                implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
            }
        }

        val iosTest by getting {
            dependencies {
                dependencies {
                    implementation(Dependency.multiplatform.kotlin.testCommon)
                    implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
                }
            }
        }
    }
}

android {
    compileSdk = LibraryConfig.android.compileSdkVersion
    resourcePrefix = LibraryConfig.android.resourcePrefix

    defaultConfig {
        minSdk = LibraryConfig.android.minSdkVersion
        targetSdk = LibraryConfig.android.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments.putAll(
            mapOf("clearPackageData" to "true")
        )
    }

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
