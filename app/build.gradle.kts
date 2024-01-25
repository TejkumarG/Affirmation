plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = AppsConfig.compileSdk

    defaultConfig {
        minSdk = AppsConfig.minSdk
        targetSdk = AppsConfig.targetSdk
        applicationId = AppsConfig.applicationId

        versionCode = AppsConfig.versionCode
        versionName = AppsConfig.versionName

        testOptions {
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "DEBUG_OR_STAGING", "true")
        }
        create("staging") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "DEBUG_OR_STAGING", "true")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "DEBUG_OR_STAGING", "false")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.material)

    implementation(libs.play.core)
    implementation(libs.play.core.ktx)

    implementation(libs.androidx.startup)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.process)

    // persistence
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    kapt(libs.dagger.android.processor)
    implementation(libs.hilt.work)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.timber)

    // Coil
    implementation(libs.coil)

    // https://github.com/chrisbanes/insetter
    implementation(libs.insetter)

    implementation(libs.backendless)

    implementation(libs.socket.io) {
        //      excluding org.json which is provided by Android
        exclude("org.json", "json")
    }

    // sdp and ssp
    implementation(libs.sdp)
    implementation(libs.ssp)

    implementation(libs.androidx.work)

    debugImplementation(libs.leakcanary)
    implementation(libs.anrwatchdog)
}
