@file:Suppress("kotlin:S1135", "kotlin:S117", "kotlin:S100", "kotlin:S3776", "kotlin:S125", "kotlin:S1128", "UNUSED_PARAMETER", "unused", "RedundantVisibilityModifier") //NOSONAR

package dependencies

object Dependencies { //NOSONAR

    const val minSdk = 21 //NOSONAR
    const val targetSdk = 28 //NOSONAR
    const val compileSdk = 28 //NOSONAR

    object Versions { //NOSONAR
        const val nanoHttp = "2.3.1" //NOSONAR
        const val crashlytics = "2.9.9" //NOSONAR
        const val dashClockApi = "2.0.0" //NOSONAR
        const val fastScroll = "1.0.20" //NOSONAR
        const val glide = "3.8.0" //NOSONAR
        const val glideOkhttp = "1.4.0@aar" //NOSONAR
        const val materialDialogs = "0.9.6.0" //NOSONAR
        const val permiso = "0.3.0" //NOSONAR
        const val streams = "1.2.1" //NOSONAR
        const val butterknife = "8.8.1" //NOSONAR
        const val butterknifeAnnotationProcessor = "8.8.1" //NOSONAR
        const val dagger = "2.21" //NOSONAR
        const val daggerAssistedInject = "0.3.2" //NOSONAR
        const val expandableRecyclerView = "3.0.0-RC1" //NOSONAR
        const val billing = "1.2" //NOSONAR
    }

    // Kotlin

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Plugins.Versions.kotlin}" //NOSONAR
    const val ktx = "androidx.core:core-ktx:${Plugins.Versions.ktx}" //NOSONAR

    // NanoHttp - https://github.com/NanoHttpd/nanohttpd (Various)
    const val nanoHttp = "org.nanohttpd:nanohttpd-webserver:${Versions.nanoHttp}" //NOSONAR

    // Crashlytics - https://fabric.io/kits/android/crashlytics
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}" //NOSONAR

    // Dashclock - https://git.io/vix9g (Roman Nurik)
    const val dashClockApi = "com.google.android.apps.dashclock:dashclock-api:${Versions.dashClockApi}" //NOSONAR

    // RecyclerView-FastScroll - https://git.io/vix5z
    const val fastScroll = "com.simplecityapps:recyclerview-fastscroll:${Versions.fastScroll}" //NOSONAR

    // Glide - https://git.io/vtn9K (Bump)
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}" //NOSONAR

    // Glide - OkHttp  integration - https://git.io/vihvW (Bump)
    const val glideOkhttp = "com.github.bumptech.glide:okhttp3-integration:${Versions.glideOkhttp}" //NOSONAR

    // Material Dialogs - https://git.io/vixHf (Aidan Follestad)
    const val materialDialogs = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}" //NOSONAR
    const val materialDialogCommons = "com.afollestad.material-dialogs:commons:${Versions.materialDialogs}" //NOSONAR

    // Permiso - https://git.io/vixQ4 (Greyson Parrelli)
    const val permiso = "com.greysonparrelli.permiso:permiso:${Versions.permiso}" //NOSONAR

    // Streams Backport - https://git.io/vCazA (Victor Melnik)
    const val streams = "com.annimon:stream:${Versions.streams}" //NOSONAR

    // Butterknife
    const val butterknife = "com.jakewharton:butterknife:${Versions.butterknife}" //NOSONAR
    const val butterknifeAnnotationProcessor = "com.jakewharton:butterknife-compiler:${Versions.butterknifeAnnotationProcessor}" //NOSONAR

    // Dagger
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}" //NOSONAR
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}" //NOSONAR
    const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}" //NOSONAR
    const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}" //NOSONAR

    // Dagger Assisted Inject
    const val daggerAssistedInject = "com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.daggerAssistedInject}" //NOSONAR
    const val daggerAssistedInjectProcessor = "com.squareup.inject:assisted-inject-processor-dagger2:${Versions.daggerAssistedInject}" //NOSONAR

    // Expandable Recycler View - https://github.com/thoughtbot/expandable-recycler-view
    const val expandableRecyclerView = "com.bignerdranch.android:expandablerecyclerview:${Versions.expandableRecyclerView}" //NOSONAR

    // In app purchases
    const val billing = "com.android.billingclient:billing:${Versions.billing}" //NOSONAR

    object Plugins { //NOSONAR

        object Versions { //NOSONAR
            const val androidGradlePlugin = "3.3.1" //NOSONAR
            const val kotlin = "1.3.21" //NOSONAR
            const val ktx = "1.0.0" //NOSONAR
            const val dexcountGradlePlugin = "0.8.6" //NOSONAR
            const val fabricGradlePlugin = "1.+" //NOSONAR
            const val gradleVersions = "0.20.0" //NOSONAR
            const val playServices = "4.2.0" //NOSONAR
        }

        const val android = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}" //NOSONAR
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" //NOSONAR
        const val dexcount = "com.getkeepsafe.dexcount:dexcount-gradle-plugin:${Versions.dexcountGradlePlugin}" //NOSONAR
        const val fabric = "io.fabric.tools:gradle:${Versions.fabricGradlePlugin}" //NOSONAR
        const val playPublisher = "com.github.triplet.play" //NOSONAR
        const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}" //NOSONAR
        const val playServices = "com.google.gms:google-services:${Versions.playServices}" //NOSONAR
    }

    object Google { //NOSONAR

        object Versions { //NOSONAR
            const val supportLib = "28.0.0" //NOSONAR
            const val firebaseCore = "16.0.5" //NOSONAR
            const val firebaseRemoteConfig = "16.1.0" //NOSONAR
            const val constraintLayout = "2.0.0-alpha3" //NOSONAR
            const val chromeCastFramework = "16.1.0" //NOSONAR
        }

        const val cardView = "com.android.support:cardview-v7:${Versions.supportLib}" //NOSONAR
        const val design = "com.android.support:design:${Versions.supportLib}" //NOSONAR
        const val palette = "com.android.support:palette-v7:${Versions.supportLib}" //NOSONAR
        const val recyclerView = "com.android.support:recyclerview-v7:${Versions.supportLib}" //NOSONAR
        const val supportv4 = "com.android.support:support-v4:${Versions.supportLib}" //NOSONAR
        const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}" //NOSONAR
        const val firebaseRemoteConfig = "com.google.firebase:firebase-config:${Versions.firebaseRemoteConfig}" //NOSONAR
        const val appcompat = "com.android.support:appcompat-v7:${Versions.supportLib}" //NOSONAR
        const val mediarouter = "com.android.support:mediarouter-v7:${Versions.supportLib}" //NOSONAR
        const val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}" //NOSONAR
        const val prefCompat = "com.android.support:preference-v7:${Versions.supportLib}" //NOSONAR
        const val prefCompatv14 = "com.android.support:preference-v14:${Versions.supportLib}" //NOSONAR
        const val chromeCastFramework = "com.google.android.gms:play-services-cast-framework:${Versions.chromeCastFramework}" //NOSONAR
    }

    object Square { //NOSONAR

        object Versions { //NOSONAR
            const val haha = "2.0.4" //NOSONAR
            const val leakCanary = "1.6.3" //NOSONAR
            const val okio = "2.1.0" //NOSONAR
            const val okhttp = "3.11.0" //NOSONAR
            const val retrofit = "2.4.0" //NOSONAR
            const val retrofitGson = "2.4.0" //NOSONAR
            const val sqlBrite = "2.0.0" //NOSONAR
        }

        const val haha = "com.squareup.haha:haha:${Versions.haha}" //NOSONAR
        const val leakCanaryDebug = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}" //NOSONAR
        const val leakCanaryRel = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}" //NOSONAR
        const val okio = "com.squareup.okio:okio:${Versions.okio}" //NOSONAR
        const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}" //NOSONAR
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}" //NOSONAR
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofitGson}" //NOSONAR
        const val sqlBrite = "com.squareup.sqlbrite2:sqlbrite:${Versions.sqlBrite}" //NOSONAR
    }

    object Rx { //NOSONAR

        object Versions { //NOSONAR
            const val rxAndroid = "2.1.0" //NOSONAR
            const val rxBinding = "2.2.0" //NOSONAR
            const val rxBindingAppCompat = "2.2.0" //NOSONAR
            const val rxJava = "2.1.9" //NOSONAR
            const val rxRelay = "2.1.0" //NOSONAR
            const val rxBroadcast = "2.0.0" //NOSONAR
            const val rxPrefs = "2.0.0" //NOSONAR
            const val rxKotlin = "2.3.0" //NOSONAR
            const val rxDogTag = "0.2.0" //NOSONAR
        }

        // RxJava - https://git.io/vihv0 (ReactiveX)
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}" //NOSONAR

        // rxBinding - https://git.io/vix5y (Jake Wharton)
        const val rxBinding = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}" //NOSONAR

        // rxBinding AppCompat - https://git.io/vix5y (Jake Wharton)
        const val rxBindingAppCompat = "com.jakewharton.rxbinding2:rxbinding-appcompat-v7:${Versions.rxBindingAppCompat}" //NOSONAR

        // RxJava - https://git.io/rxjava (ReactiveX)
        const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}" //NOSONAR

        // RX Image Picker - https://git.io/vix5H (MLSDev )
        const val rxImagePicker = "com.github.timusus:RxImagePicker:permission-check-fix-SNAPSHOT" //NOSONAR

        // RX Relay - https://github.com/JakeWharton/RxRelay
        const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}" //NOSONAR

        // Rx Receivers - https://github.com/f2prateek/rx-receivers
        const val rxBroadcast = "com.cantrowitz:rxbroadcast:${Versions.rxBroadcast}" //NOSONAR

        // Rx Prefs - https://github.com/f2prateek/rx-preferences
        const val rxPrefs = "com.f2prateek.rx.preferences2:rx-preferences:${Versions.rxPrefs}" //NOSONAR

        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}" //NOSONAR

        const val rxDogTag = "com.uber.rxdogtag:rxdogtag:${Versions.rxDogTag}" //NOSONAR
    }

    object Testing { //NOSONAR

        object Versions { //NOSONAR
            const val junit = "4.12" //NOSONAR
            const val espressoCore = "3.0.0" //NOSONAR
            const val assertj = "3.9.0" //NOSONAR

            // Mockito version restriction -- PowerMock does not fully support Mockito2 yet.
            // https://github.com/powermock/powermock/wiki/Mockito2_maven
            const val mockito = "2.8.47" //NOSONAR
            const val powermock = "1.7.1" //NOSONAR

            // Future note: PowerMock and Robolectric can't work together until Robolectric 3.3 is released
            // https://github.com/robolectric/robolectric/wiki/Using-PowerMock
            const val robolectric = "3.6.1" //NOSONAR
        }

        // JUnit
        const val junit = "junit:junit:${Versions.junit}" //NOSONAR

        // Espresso
        const val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espressoCore}" //NOSONAR

        // Mockito - https://github.com/mockito/mockito
        const val mockito = "org.mockito:mockito-core:${Versions.mockito}" //NOSONAR

        // Powermock - https://github.com/powermock/powermock
        const val powermock = "org.powermock:powermock-api-mockito2:${Versions.powermock}" //NOSONAR
        const val powermockjunit = "org.powermock:powermock-module-junit4:${Versions.powermock}" //NOSONAR

        // Robolectric - https://github.com/robolectric/robolectric
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}" //NOSONAR

        // AssertJ - http://joel-costigliola.github.io/assertj/
        const val assertj = "org.assertj:assertj-core:${Versions.assertj}" //NOSONAR
    }

    object Projects { //NOSONAR

        // Glide Palette - https://git.io/vix57 (Florent Champigny)
        val glidePalette = ":libraries:glidepalette" //NOSONAR

        // Internal navigation library
        val navigation = ":libraries:navigation" //NOSONAR

        // Internal recycler adapter library
        val recyclerAdapter = ":libraries:recycler-adapter" //NOSONAR

        // Multi Sheet View
        val multiSheetView = ":libraries:multisheetview" //NOSONAR

        // Aesthetic - Theming Engine
        val aesthetic = ":libraries:aesthetic" //NOSONAR
    }

    object BuildPlugins { //NOSONAR

        const val androidApplication = "com.android.application" //NOSONAR
        const val androidLibrary = "com.android.library" //NOSONAR
        const val kotlin = "kotlin-android" //NOSONAR
        const val kotlinAndroidExtensions = "kotlin-android-extensions" //NOSONAR
        const val kapt = "kotlin-kapt" //NOSONAR
        const val dexCount = "com.getkeepsafe.dexcount" //NOSONAR
        const val fabric = "io.fabric" //NOSONAR
        const val gradleVersions = "com.github.ben-manes.versions" //NOSONAR
        const val playServices = "com.google.gms.google-services" //NOSONAR
    }
}

