// Top-level build file where you can add configuration options common to all sub-projects/modules.
ext {
    extra["versionCode"] = 38
    extra["versionName"] = "5.2.0"

    extra["sdkVersion"] = 35
    extra["minSdkVersion"] = 24

    extra["hiltVersion"] = "2.52"

    extra["testInstrumentationRunner"] = "androidx.test.runner.AndroidJUnitRunner"
    extra["consumerProguardFiles"] = "consumer-rules.pro"

    extra["javaVersion"] = JavaVersion.VERSION_17

    extra["coreKtx"] = "androidx.core:core-ktx:1.15.0"
    extra["appcompat"] = "androidx.appcompat:appcompat:1.6.1"
    extra["kotlinVersion"] = "org.jetbrains.kotlin:kotlin-stdlib:2.0.21"
    extra["junit"] = "junit:junit:4.13.2"
    extra["extJunit"] = "androidx.test.ext:junit:1.1.5"
    extra["espressoCore"] = "androidx.test.espresso:espresso-core:3.5.1"
}

plugins {
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    kotlin("android") version "2.0.21" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildFile)
}