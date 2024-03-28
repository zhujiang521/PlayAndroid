// Top-level build file where you can add configuration options common to all sub-projects/modules.
ext {
    extra["versionCode"] = 20
    extra["versionName"] = "2.7.5"

    extra["sdkVersion"] = 34
    extra["minSdkVersion"] = 24

    extra["kotlinCompiler"] = "1.5.11"
    extra["composeVersion"] = "1.6.4"
    extra["hiltVersion"] = "2.51"

    extra["testInstrumentationRunner"] = "androidx.test.runner.AndroidJUnitRunner"
    extra["consumerProguardFiles"] = "consumer-rules.pro"

    extra["javaVersion"] = JavaVersion.VERSION_17

    extra["coreKtx"] = "androidx.core:core-ktx:1.12.0"
    extra["appcompat"] = "androidx.appcompat:appcompat:1.6.1"
    extra["kotlinVersion"] = "org.jetbrains.kotlin:kotlin-stdlib:1.9.23"
    extra["junit"] = "junit:junit:4.13.2"
    extra["extJunit"] = "androidx.test.ext:junit:1.1.5"
    extra["espressoCore"] = "androidx.test.espresso:espresso-core:3.5.1"
}

plugins {
    id("com.android.application") version "8.3.1" apply false
    id("com.android.library") version "8.3.1" apply false
    kotlin("android") version "1.9.23" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}