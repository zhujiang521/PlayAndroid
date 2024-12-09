plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.zj.model"

    val sdkVersion = rootProject.extra["sdkVersion"] as Int?
    val minSdkVersion = rootProject.extra["minSdkVersion"] as Int?
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = sdkVersion

        testInstrumentationRunner = rootProject.extra["testInstrumentationRunner"] as String?
        rootProject.extra["consumerProguardFiles"]?.let { consumerProguardFiles(it) }
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments += listOf(
//                        "room.schemaLocation"  : "$projectDir/schemas".toString(),
//                        "room.incremental"     : "true",
//                        "room.expandProjection": "true")
//            }
//        }
    }

    buildTypes {
        release {
            // 开启混淆
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    val javaVersion = rootProject.extra["javaVersion"] as JavaVersion
    val javaVersionName = javaVersion.toString()

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersionName))
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

dependencies {

    implementation("${rootProject.extra["kotlinVersion"] as String?}")
    implementation("${rootProject.extra["coreKtx"] as String?}")
    implementation("${rootProject.extra["appcompat"] as String?}")
    implementation(project(":core"))

    val roomVersion = "2.6.1"
    api("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // For Kotlin use kapt instead of annotationProcessor

    // optional - Kotlin Extensions and Coroutines support for Room
    api("androidx.room:room-ktx:$roomVersion")

    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
}