package br.com.shido.apollokmm.loginrepository

import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun fetchLogin(username: String, password: String): Flow<Boolean>


}