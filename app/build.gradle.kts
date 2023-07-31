plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
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
//        resourceConfigurations += ['zh', 'zh-rCN', 'zh-rHK', 'zh-rTW']
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
    val javaVersion = JavaVersion.VERSION_17
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["kotlinCompiler"] as String?
    }
    namespace = "com.zj.play"
}

dependencies {

    implementation("${rootProject.extra["coreKtx"] as String?}")
    implementation("${rootProject.extra["appcompat"] as String?}")

    val composeVersion = rootProject.extra["composeVersion"] as String?
    // Compose依赖库
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.0-rc01")
    implementation(project(":network"))

    val accompanist_version = "0.31.6-rc"
    implementation("com.google.accompanist:accompanist-webview:$accompanist_version")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist_version")

    val glanceVersion = "1.0.0-rc01"
    implementation("androidx.glance:glance:$glanceVersion")
    implementation("androidx.glance:glance-appwidget:$glanceVersion")
    implementation("androidx.glance:glance-material3:$glanceVersion")

    // Paging 分页加载
    implementation("androidx.paging:paging-compose:3.2.0")
    // lottie动画
    val lottieVersion = "6.1.0"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    // material3 动态主题颜色切换应用颜色
//    implementation("androidx.compose.material3:material3:1.0.0-alpha09"

    val hiltVersion = rootProject.extra["hiltVersion"] as String?
    // Hilt 依赖注入
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
}