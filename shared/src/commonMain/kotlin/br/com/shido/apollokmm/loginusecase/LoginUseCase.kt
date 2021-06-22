package br.com.shido.apollokmm.loginusecase

import br.com.shido.apollokmm.flowhelper.CommonFlow
import br.com.shido.apollokmm.flowhelper.asCommonFlow
import br.com.shido.apollokmm.loginrepository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {

    @Throws(Exception::class)
    suspend fun fetchLogin(username: String, password: String): CommonFlow<Boolean> =
        repository.fetchLogin(username, password).asCommonFlow()
}