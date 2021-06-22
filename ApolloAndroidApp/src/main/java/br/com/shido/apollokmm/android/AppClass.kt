package br.com.shido.apollokmm.android

import android.app.Application
import br.com.shido.apollokmm.android.di.injectLoginFeature
import br.com.shido.apollokmm.di.injectLoginSharedFeature
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@AppClass) }

        injectLoginFeature()
        injectLoginSharedFeature()
    }
}