package br.com.shido.apollokmm.logindatasource

import br.com.ampli.LoginStudentMutation
import com.apollographql.apollo.api.Response
import kotlinx.coroutines.flow.Flow

interface LoginDataSource {

    fun fetchLoginStudentAsync(username: String, password: String): Flow<Response<LoginStudentMutation.Data>>

}