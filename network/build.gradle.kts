plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zj.network"

    val sdkVersion = rootProject.extra["sdkVersion"] as Int?
    val minSdkVersion = rootProject.extra["minSdkVersion"] as Int?
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = sdkVersion

        testInstrumentationRunner = rootProject.extra["testInstrumentationRunner"] as String?
        rootProject.extra["consumerProguardFiles"]?.let { consumerProguardFiles(it) }
        resourceConfigurations += listOf("en", "zh", "zh-rCN", "zh-rHK", "zh-rTW")
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
    implementation(project(":model"))
    implementation(project(":core"))
    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
}