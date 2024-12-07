plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mobdeve.s21.ermitano.kate_justine.mco2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobdeve.s21.ermitano.kate_justine.mco2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BOM to manage Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    // authentication
    implementation("com.google.firebase:firebase-analytics")
    // Add Google Sign-In dependency
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    // database dependency
    implementation("com.google.firebase:firebase-firestore:24.4.2")
    // for loading images
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    implementation ("com.google.firebase:firebase-storage:20.2.1")
    implementation ("com.google.firebase:firebase-firestore:24.6.3")

    // MPAndroidChart
    //implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    //For Color picker
    implementation ("com.github.yukuku:ambilwarna:2.0.1")
    // Generate QR code
    implementation ("com.google.zxing:core:3.5.2")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

}
