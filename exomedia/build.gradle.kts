@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.devbrackets.android.exomedia"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        vectorDrawables {
            useSupportLibrary = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose.v140)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

//    implementation(libs.exoplayer)
//    implementation(libs.androidx.media)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    val media3Version = "1.1.0"
    api("androidx.media3:media3-exoplayer:$media3Version")
    api("androidx.media3:media3-exoplayer-dash:$media3Version")
    api("androidx.media3:media3-exoplayer-hls:$media3Version")
    api("androidx.media3:media3-exoplayer-smoothstreaming:$media3Version")
}