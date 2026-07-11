plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.aboutLibraries)
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
        versionName = "4.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            //noinspection NotShrinkingResources
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.transition)
    implementation(libs.androidx.splashscreen)
    implementation(libs.google.material)
    implementation(libs.aboutlibraries.view)
    implementation(libs.markdown.core)
    implementation(libs.markdown.linkify)
}