plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.uravgcode.chooser"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.uravgcode.chooser"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "1.3-pre-3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    dependenciesInfo {
        includeInApk = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}