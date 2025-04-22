plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Để bật Firebase
}

android {
    namespace = "com.example.doan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.doan"
        minSdk = 27
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Android cơ bản
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Unit Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BoM (bắt buộc chỉ khai báo 1 lần)
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    // Firebase SDKs
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    // Glide để load ảnh
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("androidx.cardview:cardview:1.0.0")

}
