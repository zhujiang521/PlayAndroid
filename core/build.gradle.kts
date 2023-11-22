plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zj.core"

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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar", "*.aar") })
    implementation("${rootProject.extra["kotlinVersion"] as String?}")
    implementation("${rootProject.extra["coreKtx"] as String?}")
    implementation("${rootProject.extra["appcompat"] as String?}")

    val lifecycleVersion = "2.6.2"
    api("androidx.recyclerview:recyclerview:1.3.1")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    api("com.google.android.material:material:1.9.0")

    val retrofitVersion = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api ("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    val coroutinesVersion = "1.7.3"
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    api ("androidx.datastore:datastore-preferences:1.0.0")

    val refreshVersion = "2.1.0"
    api ("io.github.scwang90:refresh-layout-kernel:$refreshVersion")      //核心必须依赖
    api ("io.github.scwang90:refresh-header-classics:$refreshVersion")    //经典刷新头
    //api  'com.scwang.smart:refresh-header-material:2.0.1'    //谷歌刷新头
    api ("io.github.scwang90:refresh-footer-classics:$refreshVersion")   //经典加载

    // 安卓Utils
//    api 'com.blankj:utilcode:1.30.7'

    // 图片网络框架
    val glideVersion = "4.16.0"
    api ("com.github.bumptech.glide:glide:$glideVersion")
    annotationProcessor("com.github.bumptech.glide:compiler:$glideVersion")

    //Lottie动画
    implementation ("com.airbnb.android:lottie:6.1.0")

    val activityVersion = "1.7.2"
    api ("androidx.activity:activity-ktx:$activityVersion")
    val fragmentVersion = "1.6.0"
    api ("androidx.fragment:fragment-ktx:$fragmentVersion")

    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
}
