package br.com.shido.apollokmm.di

import br.com.shido.apollokmm.logindatasource.LoginApollo
import br.com.shido.apollokmm.logindatasource.LoginDataSource
import br.com.shido.apollokmm.loginrepository.LoginRepository
import br.com.shido.apollokmm.loginrepository.LoginRepositoryImp
import br.com.shido.apollokmm.loginusecase.LoginUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectLoginSharedFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(repository, useCase, datasource)
    )
}

private val repository = module {
    single<LoginRepository> { LoginRepositoryImp(get()) }
}

private val useCase = module {
    factory { LoginUseCase(get()) }
}

private val datasource = module {
    single<LoginDataSource> { LoginApollo() }
}