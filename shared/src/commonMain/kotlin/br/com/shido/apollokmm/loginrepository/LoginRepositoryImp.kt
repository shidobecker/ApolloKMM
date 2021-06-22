package br.com.shido.apollokmm.loginrepository

import br.com.shido.apollokmm.logindatasource.LoginDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf

class LoginRepositoryImp(private val dataSource: LoginDataSource) : LoginRepository {

    @Throws(Exception::class)
    override suspend fun fetchLogin(username: String, password: String): Flow<Boolean> {
        val apolloResult = dataSource.fetchLoginStudentAsync(username, password)

        lateinit var result: Flow<Boolean>

        apolloResult.collect { response ->
            if (response.hasErrors()) {
                throw Exception()
            } else {
                result = flowOf(true)
            }
        }

        return result
    }
}