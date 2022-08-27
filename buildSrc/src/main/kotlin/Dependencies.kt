object App {
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions {
    const val kotlin         = "1.7.0"
    const val gradle         = "7.2.2"
    const val sqlDelight     = "1.5.3"

    const val compose         = "1.2.0"
    const val coil            = "2.10"
    const val activityCompose = "1.5.1"
    const val navigation      = "2.5.1"

    const val material        = "1.6.1"

    const val coroutines = "1.6.4"
    const val koin       = "3.2.0"
    const val ktor       = "2.1.0"

    const val minSdk     = 23
    const val compileSdk = 32
    const val targetSdk  = 32

    const val realm = "1.0.2"

    const val moko = "0.13.1"

    const val kermit = "0.3.0-m1"

    const val kotlinxSerializationCore = "1.3.3"
    const val kotlinxCoroutinesCore    = "1.6.3-native-mt"
}

object Libraries {
    const val kotlin              = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val gradle              = "com.android.tools.build:gradle:${Versions.gradle}"
    const val sqlDelight          = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"

    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val koinAndroid       = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    const val koinCore          = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinKtor              = "io.insert-koin:koin-ktor:${Versions.koin}"

    const val ktorCore              = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorSerialization     = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val ktorAndroid           = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val ktorClientLogging     = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val ktorContentNegotiation= "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    const val ktorJson              = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    const val ktorClientJson    = "io.ktor:ktor-client-json:${Versions.ktor}"

    const val composeDestinationPlugin = "io.github.raamcosta.compose-destinations:ksp:$1.2.0"

    const val realmPlugin    = "io.realm.kotlin:gradle-plugin:${Versions.realm}"
    const val realm             = "io.realm.kotlin:library-base:${Versions.realm}"

    const val mokoMVVMCore = "dev.icerock.moko:mvvm-core:${Versions.moko}"

    const val kermitLogger = "co.touchlab:kermit:${Versions.kermit}"

    object Compose {
        const val activity         = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val ui               = "androidx.compose.ui:ui:${Versions.compose}"
        const val material         = "androidx.compose.material:material:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val navigation       = "androidx.navigation:navigation-compose:${Versions.navigation}"
        const val coil             = "io.coil-kt:coil-compose:2.1.0"
    }

    object Common {
        const val sqlDelight               = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val sqlDelightExtension      = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
        const val kotlinxSerializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerializationCore}"
        const val kotlinxCoroutinesCore    = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutinesCore}"
    }

    object Android {
        const val sqlDelight = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    }

    object IOs {
        const val ktorClient = "io.ktor:ktor-client-ios:${Versions.ktor}"
        const val sqlDelight = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
    }

}

object SupportLibraries {
    const val material = "com.google.android.material:material:${Versions.material}"
}

