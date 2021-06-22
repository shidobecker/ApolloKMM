package br.com.shido.apollokmm.loginrepository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    @Throws(Exception::class)
    suspend fun fetchLogin(username: String, password: String): Flow<Boolean>


}