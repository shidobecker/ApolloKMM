package br.com.shido.apollokmm.android.di

import br.com.shido.apollokmm.android.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun injectLoginFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            viewModel
        )
    )
}

private val viewModel = module {
    viewModel { MainViewModel(get()) }
}