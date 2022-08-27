plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "kim.young.fakestoreapp.android"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = App.versionCode
        versionName = App.versionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(Libraries.koinAndroid)

    implementation(SupportLibraries.material)

    implementation(Libraries.coroutinesAndroid)
    implementation(Libraries.ktorCore)
    implementation(Libraries.ktorSerialization)
    implementation(Libraries.ktorAndroid)

    implementation(Libraries.Compose.ui)
    implementation(Libraries.Compose.material)
    implementation(Libraries.Compose.uiToolingPreview)
    implementation(Libraries.Compose.coil)
    implementation(Libraries.Compose.activity)
    implementation(Libraries.Compose.navigation)

//    implementation(Libraries.composeDestinationPlugin)

}