import java.net.URL

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization") version "2.1.21"
}

task("downloadTFLiteModels") {
    doLast {
        val assetDir = file("src/main/assets")
        if (!assetDir.exists()) {
            assetDir.mkdirs()
        }

        val models = mapOf(
            "mobilebert.tflite" to "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/text_classification/android/mobilebert.tflite",
            "wordvec.tflite" to "https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/text_classification/android/text_classification_v2.tflite"
        )

        models.forEach { (fileName, url) ->
            val destination = file("${assetDir.path}/$fileName")
            if (!destination.exists()) {
                println("Downloading $fileName...")
                URL(url).openStream().use { input ->
                    destination.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                println("Download complete for $fileName.")
            } else {
                println("$fileName already exists. Skipping download.")
            }
        }
    }
}

// Menjalankan tugas unduh sebelum build
tasks.named("preBuild").configure {
    dependsOn("downloadTFLiteModels")
}

android {
    namespace = "com.allano.alquran"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.allano.alquran"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    aaptOptions {
        noCompress("tflite")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    // Retrofit untuk Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // KotlinX Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Coroutines & Flow
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("io.coil-kt:coil:2.6.0")
    implementation("org.tensorflow:tensorflow-lite-task-text:0.4.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}