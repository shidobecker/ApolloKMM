plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":shared"))
    implementation(SupportLibraries.design)
    implementation(SupportLibraries.appcompat)
    implementation(SupportLibraries.constraintLayout)

    implementation(Libraries.ktxCore)
    implementation(Libraries.ktxFragment)
    implementation(JetPackLibraries.lifecycleViewModelKtx)
    implementation(JetPackLibraries.lifecycleLivedataKtx)

    // Koin main features for Android (Scope,ViewModel ...)
    implementation(Libraries.Koin.koinAndroid)

    // Koin for Jetpack WorkManager
    implementation(Libraries.Koin.koinWorkManager)

    // Koin for Jetpack Compose (unstable version)
    implementation(Libraries.Koin.koinCompose)

    testImplementation(Libraries.Koin.koinJUnit4)
    testImplementation(Libraries.Koin.koinJUnit5)

    implementation(Libraries.viewBindingDelegate)
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "br.com.shido.apollokmm.android"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}