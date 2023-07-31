plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {

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
    namespace = "com.zj.network"
}

dependencies {

    implementation("${rootProject.extra["coreKtx"] as String?}")
    implementation("${rootProject.extra["appcompat"] as String?}")
    api(project(":model"))
    api(project(":utils"))
    // 网络请求
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
}