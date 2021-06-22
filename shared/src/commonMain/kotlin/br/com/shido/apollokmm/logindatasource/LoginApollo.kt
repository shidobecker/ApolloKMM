package br.com.shido.apollokmm.logindatasource

import br.com.ampli.LoginStudentMutation
import br.com.ampli.type.Credentials
import br.com.shido.apollokmm.apollo.GraphQLApolloClient
import com.apollographql.apollo.api.Response
import kotlinx.coroutines.flow.Flow

class LoginApollo : LoginDataSource {

    override fun fetchLoginStudentAsync(
        username: String,
        password: String
    ): Flow<Response<LoginStudentMutation.Data>> {
        val credential = Credentials(
            username = username, password = password
        )

        val loginMutation = LoginStudentMutation(credential)

        return GraphQLApolloClient.createClient().mutate(loginMutation).execute()
    }
}