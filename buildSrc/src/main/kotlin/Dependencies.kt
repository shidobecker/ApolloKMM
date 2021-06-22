
const val kotlinVersion = "1.4.0"
const val coroutinesVersion = "1.4.3"

object BuildPlugins {

    object Versions {
        const val gradlePluginVersion = "4.0.0"
    }

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePluginVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val kapt = "kotlin-kapt"
    const val androidLibrary = "com.android.library"
    const val androidDynamicFeature = "com.android.dynamic-feature"


}

object AndroidSdk {
    const val min = 21
    const val buildToolsVersion = "29.0.2"
    const val compileSdkVersion = 29
    const val target = compileSdkVersion
}



object Libraries {
    object Versions {
        //Android
        const val ktx = "1.3.2"
        const val ktxFragment = "1.2.5"

        const val apolloGraphql = "2.5.6"

        const val apolloCache = "2.5.6"
        const val apolloCoroutines = "2.5.6"

        const val viewBindingDelegate = "1.4.5"

    }

    object Koin {
        const val koinVersion = "3.1.0"

        const val koinCore = "io.insert-koin:koin-core:$koinVersion"

        const val koinTest = "io.insert-koin:koin-test:$koinVersion"

        // Koin main features for Android (Scope,ViewModel ...)
        const val koinAndroid = "io.insert-koin:koin-android:$koinVersion"

        // Koin for Jetpack WorkManager
        const val koinWorkManager =
            "io.insert-koin:koin-androidx-workmanager:$koinVersion"

        // Koin for Jetpack Compose (unstable version)
        const val koinCompose = "io.insert-koin:koin-androidx-compose:$koinVersion"

        // Koin for JUnit 4
        const val koinJUnit4 = "io.insert-koin:koin-test-junit4:$koinVersion"

        // Koin for JUnit 5
        const val koinJUnit5 = "io.insert-koin:koin-test-junit5:$koinVersion"
    }

    object Apollo {
        const val apolloVersion = "2.5.9"

        const val apolloRuntimeKotlin = "com.apollographql.apollo:apollo-runtime-kotlin:$apolloVersion"

    }

    //Kotlin
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val androidCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"


    const val ktxCore = "androidx.core:core-ktx:${Versions.ktx}"
    const val ktxFragment = "androidx.fragment:fragment-ktx:${Versions.ktxFragment}"

    //GraphQL
    const val apolloRuntime = "com.apollographql.apollo:apollo-runtime:${Versions.apolloGraphql}"
    const val apolloAndroidPlugin = "com.apollographql.apollo"
    const val graphqlGradlePlugin =
        "com.apollographql.apollo:apollo-gradle-plugin:${Versions.apolloGraphql}"
    const val apolloCache = "com.apollographql.apollo:apollo-http-cache:${Versions.apolloCache}"
    const val apolloCoroutines =
        "com.apollographql.apollo:apollo-coroutines-support:${Versions.apolloCoroutines}"



    const val viewBindingDelegate =
        "com.github.kirich1409:viewbindingpropertydelegate:${Versions.viewBindingDelegate}"


}


object JetPackLibraries {
    object Versions {
        const val lifecycle = "2.1.0"
        const val navigation = "2.3.1"
        const val workManager = "2.2.0"
    }

    //Lifecycle
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc03"
    const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-rc03"


    //Navigation
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"


}



object SupportLibraries {
    private object Versions {
        //Support Library
        const val appcompat = "1.2.0"
        const val materialDesign = "1.2.0"
        const val cardView = "1.0.0"
        const val recyclerView = "1.0.0"
        const val constraintLayout = "2.0.4"
    }

    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val design = "com.google.android.material:material:${Versions.materialDesign}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
}


object TestLibraries {
    private object Versions {
        const val junit4 = "4.12"
        const val testCore = "1.2.0"
        const val testRunner = "1.2.0"
        const val espresso = "3.2.0"
        const val mockk = "1.11.0"
        const val truth = "1.0"

    }

    //Android Test Core
    const val junit4 = "junit:junit:${Versions.junit4}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val testCore = "androidx.test:core:${Versions.testCore}"

    //Coroutines test
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"

    //Mockk For Android Instrumented Testing
    const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"

    //Mockk For Unit Testing
    const val mockk = "io.mockk:mockk:${Versions.mockk}"

    //Architecture Components Testing
    const val archTestCore =
        "androidx.arch.core:core-testing:${JetPackLibraries.Versions.lifecycle}"
    const val workManagerTesting =
        "androidx.work:work-testing:${JetPackLibraries.Versions.workManager}"


    const val truth = "com.google.truth:truth:${Versions.truth}"
}
