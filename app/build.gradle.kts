import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id ("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.veeci.vocabhelper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.veeci.vocabhelper"
        minSdk = 24
        targetSdk = 34
        versionCode = 9
        versionName = "1.0.8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        archivesName = "vocabhelper-v${versionName}(${versionCode})"
    }

    buildFeatures{
        viewBinding = true
    }
    signingConfigs {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()

        if (keystorePropertiesFile.exists()) {
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        }
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
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

    implementation(project(":base"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.firebase.crashlytics)
    implementation(libs.material.v190)

    //Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    //Lifecycle
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)


    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)

    //Indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    //Searchbar
    implementation("com.github.mancj:MaterialSearchBar:0.8.5")

    //Room
    implementation( libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation( libs.androidx.room.ktx)

    //Firebase
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)

    //Coil
    implementation(libs.coil)

    //Biometric
    implementation(libs.androidx.biometric)

    //Cryptography
    implementation(libs.androidx.security.crypto)

    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.22")
}