plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version Versions.kotlin
    id("io.realm.kotlin")
    id("kotlin-parcelize")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()

    kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {

        // export correct artifact to use all classes of library directly from Swift
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export("dev.icerock.moko:mvvm-core:0.13.1")
        }

        binaries.all {
            binaryOptions["memoryModel"] = "experimental"
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

                implementation(Libraries.ktorCore)
                implementation(Libraries.ktorJson)
                implementation(Libraries.ktorClientJson)
                implementation(Libraries.ktorClientLogging)
                implementation(Libraries.ktorSerialization)
                implementation(Libraries.ktorContentNegotiation)

                implementation(Libraries.Common.kotlinxSerializationCore)
                implementation(Libraries.Common.kotlinxCoroutinesCore)

                implementation(Libraries.koinCore)

                implementation(Libraries.realm)
                implementation("io.github.aakira:napier:2.6.1")

                api(Libraries.kermitLogger)
                api(Libraries.mokoMVVMCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
//                implementation(Libraries.Android.sqlDelight)
                implementation(Libraries.ktorAndroid)
                implementation(Libraries.koinAndroid)
                implementation(Libraries.coroutinesAndroid)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        //val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
                implementation(Libraries.IOs.ktorClient)
                implementation(Libraries.IOs.sqlDelight)
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            //iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        //val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            //iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = Versions.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
}
