package br.com.shido.apollokmm.loginusecase

import br.com.shido.apollokmm.flowhelper.asCommonFlow
import br.com.shido.apollokmm.loginrepository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {

    suspend fun fetchLogin(username: String, password: String) =
        repository.fetchLogin(username, password).asCommonFlow()
}