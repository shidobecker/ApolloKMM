package br.com.shido.apollokmm.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.apollographql.apollo.network.http.ApolloHttpNetworkTransport

class GraphQLApolloClient {

    companion object {

        fun createClient(): ApolloClient {

            val client = ApolloClient(
                networkTransport = ApolloHttpNetworkTransport(
                    serverUrl = "https://graphql.academico.int.ampli.com.br",
                    headers = mapOf(
                        "Authorization" to "Basic bW9iaWxlOnNlY3JldA=="
                    )
                ),


             )

            val moneyCustomAdapter = getMoneyTypeAdapter()

            return client
        }




        private fun getMoneyTypeAdapter(): CustomTypeAdapter<Float> {
            return object : CustomTypeAdapter<Float> {
                override fun encode(value: Float): CustomTypeValue<*> {
                    return CustomTypeValue.GraphQLString(value.toString())
                }

                override fun decode(value: CustomTypeValue<*>): Float {
                    val stringValue = value.value.toString()
                    return try {
                        stringValue.toFloat()
                    } catch (ex: Exception) {
                        0F
                    }
                }
            }
        }
    }


}