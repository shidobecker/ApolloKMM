package br.com.shido.apollokmm.di

import br.com.shido.apollokmm.logindatasource.LoginApollo
import br.com.shido.apollokmm.loginrepository.LoginRepositoryImp
import br.com.shido.apollokmm.loginusecase.LoginUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val iosModule = module {
    factory { LoginUseCase(get()) }
}

fun initIOSDependencies() = startKoin {
    modules(commonModule, iosModule)
}

class LoginIOSComponent: KoinComponent {
    fun provideLoginUseCase(): LoginUseCase = get()
}