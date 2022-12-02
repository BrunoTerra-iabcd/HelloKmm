plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.7.21"
    id("com.squareup.sqldelight")
    id("dev.jamiecraane.plugins.kmmresources") version "1.0.0-alpha10"
}

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    val coroutinesVersion = "1.6.4"
    val ktorVersion = "2.1.2"
    val sqlDelightVersion = "1.5.3"
    val dateTimeVersion = "0.4.0"

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "com.abcd.hellokmm.database"
        sourceFolders = listOf("sqldelight")
    }
}

kmmResourcesConfig {
    androidApplicationId.set("com.abcd.hellokmm")
    packageName.set("com.abcd.hellokmm")
    defaultLanguage.set("pt")
    input.set(File(project.projectDir.path, "strings.yaml"))
    output.set(project.projectDir)
    srcFolder.set("src") // Optional, defaults to 'src'
    generatedClassName.set("KMMResourcesLocalization.kt") // Optional, defaults to 'KMMResourcesLocalization.kt'
    androidStringsPrefix.set("_generated") // Optional, defaults to '_generated'
    androidSourceFolder.set("main") // The location of the android sources in the shared module (Optional, defaults to androidMain)
    useDefaultTranslationIfNotInitialized.set(true) // When true, outputs the texts of the default language in the Android generated actual declarations instead of an empty String

    val generateLocalizations = tasks["generateLocalizations"]
    tasks["preBuild"].dependsOn(generateLocalizations)
}

android {
    namespace = "com.abcd.hellokmm"
    compileSdk = 32
    defaultConfig {
        minSdk = 23
        targetSdk = 32
    }
}
