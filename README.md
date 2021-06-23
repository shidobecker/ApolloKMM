# ApolloKMM

 # Proposta de arquitetura Multiplatform Mobile
 
A idéia desse documento é explicar e exemplificar uma nova arquitetura Mobile para os clients de Android e iOS baseada no [Kotlin Multiplatform Mobile](https://kotlinlang.org/docs/mobile/getting-started.html), listando seus módulos, fluxos de dados, exceptions, injeções de dependencias e camadas.

## Módulos

### Módulo Shared 
Dentro do projeto KMM o módulo *Shared* representa toda a parte que será compartilhada entre as plataformas
![Screen Shot 2021-06-22 at 21 03 30](https://user-images.githubusercontent.com/13834922/123014822-6dc6bf80-d39d-11eb-8358-de384d2b365e.png). 

Sendo seu arquivo de build : `build.gradle.kts` o mais importante para a configuração, nele irão configuraçoes das partes em comum de ambas as platformas assim como algumas especificas:

- Configurações de plugins. 

![Screen Shot 2021-06-22 at 21 06 28](https://user-images.githubusercontent.com/13834922/123015024-d6ae3780-d39d-11eb-9498-7e4355627e6c.png) 

- Configurações do Kotlin Native. 

![Screen Shot 2021-06-22 at 21 06 19](https://user-images.githubusercontent.com/13834922/123015050-e3cb2680-d39d-11eb-90b2-5d056a0cfaf8.png) 


Dentro de sources sets, vāo as dependencias compartilhadas (todas native). 
![Screen Shot 2021-06-22 at 21 08 28](https://user-images.githubusercontent.com/13834922/123015120-0eb57a80-d39e-11eb-943b-1800544aa734.png)

Para o build de iOS a task `packForXcode` é essencial:

 
``` kotlin
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>("ios").binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
```



## APIs utilizadas:
- Para network/graphql: *Apollo* : `com.apollographql.apollo:apollo-runtime-kotlin` [Apollo Multiplatform](https://www.apollographql.com/docs/android/essentials/get-started-multiplatform/)
- Para banco de dados: *** A DEFINIR ****
- Para injeção de dependencia: *Koin* `io.insert-koin:koin-core`  [Koin Multiplatform](https://insert-koin.io/docs/setup/v3)
- Para Multithreading/ Async : *Coroutines* `org.jetbrains.kotlinx:kotlinx-coroutines-core` [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)


### DataFlow
 ![Screen Shot 2021-06-22 at 21 39 06](https://user-images.githubusercontent.com/13834922/123017103-4e7e6100-d3a2-11eb-9ce7-e8fce775d2b3.png)




## Layers dentro do módulo shared
### DataSource - Network - GraphQL Apollo
 Responsável pelo fetch das informações ou o envio das mesmas para um backend e retorna-las na forma de `Flow<Response<T>>` para a camada superior `Repository`
 
 ``` kotlin
 
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
 
 ```
 
 A pasta graphql continua dentro de commonMain com o schema e suas queries e mutations para que as classes sejam devidamente geradas
 
 
### Repository - Local Cache
O repository será aquele que irá fazer o acesso ao datasource, buscar dados, salva-los no banco/cache local e retorna-los na forma de `Flow<T>` para a camada superior `UseCase`

``` kotlin

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

```

### Use case - Regras de negocio
O use case será responsavel por fazer o acesso ao repository, resgatar os dados, e lançar alguma exception no caso de dados null/empty de acordo com a necessidade, nele serāo retornados dados do tipo `CommonFlow<T>`

```kotlin
 @Throws(Exception::class)
    suspend fun fetchLogin(username: String, password: String): CommonFlow<Boolean> =
        repository.fetchLogin(username, password).asCommonFlow()

```

A classe *CommonFlow* serve como um wrapper para que o iOS consiga interpretar mais corretamente o Flow:
```kotlin

fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)

class CommonFlow<T>(private val origin: Flow<T>): Flow<T> by origin {
    fun collectCommon(
        coroutineScope: CoroutineScope? = null, // 'viewModelScope' on Android and 'nil' on iOS
        callback: (T) -> Unit, // callback on each emission
    ){
        onEach {
            callback(it)
        }.launchIn(coroutineScope ?: CoroutineScope(Dispatchers.Main))
    }
}


```


