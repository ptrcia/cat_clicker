plugins {
    alias(libs.plugins.android.application) // Solo necesitas este alias si está definido en el build.gradle root
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.patriciagracia.catclicker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.patriciagracia.catclicker"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17 // Cambiado del 11 al 17
        targetCompatibility = JavaVersion.VERSION_17
    }


    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.material)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.junit.junit)

    // En teoría esto está desfasado
    // implementation(libs.legacy.support.v4)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    //glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    kapt(libs.room.compiler) // para room

    implementation(libs.gson)
}