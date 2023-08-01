plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zj.play"

    val sdkVersion = rootProject.extra["sdkVersion"] as Int?
    val minSdkVersion = rootProject.extra["minSdkVersion"] as Int?
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = sdkVersion

        versionCode = rootProject.extra["versionCode"] as Int?
        versionName = rootProject.extra["versionName"] as String?

        testInstrumentationRunner = rootProject.extra["testInstrumentationRunner"] as String?

        vectorDrawables {
            useSupportLibrary = true
        }
        resourceConfigurations += listOf("en", "zh", "zh-rCN", "zh-rHK", "zh-rTW")
    }

    buildTypes {
        release {
            // 开启混淆
            isMinifyEnabled = true
            // 去除无用资源 与lint不同
            isShrinkResources = true
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

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar", "*.aar") })
    implementation("${rootProject.extra["kotlinVersion"] as String?}")
    implementation("${rootProject.extra["coreKtx"] as String?}")
    implementation("${rootProject.extra["appcompat"] as String?}")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("io.github.youth5201314:banner:2.2.2")

    implementation(project(":model"))
    implementation(project(":core"))
    implementation(project(":network"))

    // 内存泄漏检测
    //implementation 'com.squareup.leakcanary:leakcanary-android:1.5.4'

    // hilt
    val hiltVersion = rootProject.extra["hiltVersion"] as String?
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
    ksp("androidx.hilt:hilt-compiler:1.0.0")

    // 异常上报
    implementation("com.tencent.bugly:crashreport:4.1.9")
    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
}
