package br.com.shido.apollokmm.di

import br.com.shido.apollokmm.loginusecase.LoginUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.module


fun initIosDependencies() = startKoin {
    modules(commonModule, iosModule)
}

private val iosModule = module {
    factory { LoginUseCase(get()) }

}

/**
 * This is a DI Component exposed for our Swift code. It contains all the business classes
 * that matter for the iOS app.
 */
class IosComponent : KoinComponent {
    fun provideLoadUserUseCase(): LoginUseCase = get()
}