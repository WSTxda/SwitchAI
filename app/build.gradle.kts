plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.aboutLibraries)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.wstxda.switchai"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.wstxda.switchai"
        minSdk = 26
        targetSdk = 37
        versionCode = 410
        versionName = "4.0.0"
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        aidl = true
        compose = true
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.splashscreen)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.miuix.ui)
    implementation(libs.miuix.preference)
    implementation(libs.miuix.icons)
    implementation(libs.miuix.nav)
    implementation(libs.miuix.blur)
    implementation(libs.miuix.squircle)
    implementation(libs.aboutlibraries.compose.core)
    implementation(libs.kotlinx.serialization)
    implementation(libs.markdown)
    implementation(libs.reorderable)
}
