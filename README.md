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
https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6


# Koin
https://www.futuremind.com/blog/handling-kotlin-multiplatform-coroutines-koru
https://github.com/FutureMind/koru-example

# Extra content:

Add dependencies to another KMM Module:
https://kotlinlang.org/docs/mobile/add-dependencies.html#dependency-on-another-multiplatform-project

Referencias:

https://github.com/mitchtabian/Food2Fork-KMM
https://github.com/dbaroncelli/D-KMP-sample



# Implicações do uso de KMM

### Refacs
Serao necessarios diversos refac para que possamos juntar os apps mobile com um modulo multiplatform
Alguns exemplos do que devem ser feitos estao nos padroes de expect/actual e mudar suas implementaçoes de acordo com a plataforma (por exemplo: geraçao de UUID)
Os codigos devem ser transferidos dos seus respectivos modulos atuais para o modulo compartilhado, alem disso todos os testes devem ser refeitos para estarem de acordo com a nova implementaçao


### Datas
Usando o `kotlinx.datetime` será possivel unificar toda a parte de data/hora 

### Banco de dados
Para que possamos implementar um banco de dados de forma compartilhada, alguns pontos precisam ser considerados
- Todos os dados que existirem atualmente no app serão apagados, ja que independente de qual banco seja utilizado, novos files serão criados com uma nova estrutura, deixando os arquivos db atuais como inutilizados.
- Todos os repositories precisarão ser re-implementados para o salvamento de dados no novo banco, todos os testes referentes devem ser alterados e retestados. O que gerará um grande refac de toda essa parte.
- Para bancos como Realm e KodeinDB: Necessidade de criar um objeto Entidade e um objeto Entidade-UI, assim como o datamapper dos mesmos para isolar os objetos do banco no modulo shared (Por eles estenderem de RealmObject ou de Metadata - Não é possível acessar as propriedades deles sem ter as bibliotecas adicionadas no modulo, por exemplo : user.name nao estaria acessivel, por isso a conversão seria necessaria)

Não temos até o momento (23/06/2021) um banco de dados confiavel e estavel que possamos utilizar dentro do KMM de maneira compartilhada. 
As opçoes que temos hoje:

- [Realm](https://github.com/realm/realm-kotlin)
 Atualmente em fase Alpha de desenvolvimento ainda sem suporte a RealmList (23/06/2021) e sem informações sobre **Migration**. 
 
- [KodeinDB](https://github.com/Kodein-Framework/Kodein-DB) 
 Atualmente em fase Beta de desenvolvimento. Configuraçao um pouco complicada de ser feita, necessidade de estender a entidade de um Metadata e serializable do Kotlin.  
Vantagens: Não possui schema - Sem migração

 - [SqlDelight](https://github.com/cashapp/sqldelight)
 Dos bancos atuais é o com mais estabilidade de versao
Vantagens: Funciona bem integrando com Android e iOS implementando seus respectivos Drivers. 
Desvantagens: 
 - Todo o Schema deve ficar em um arquivo .db dentro de uma pasta especifica, nao pode ser dividido em outros arquivos e nem em outras pastas ou outros modulos.  
 - Não somente o schema mas todas as queries SQL de **CREATE, DELETE, UPDATE, SELECT** assim como todas as **CREATE_TABLE, ALTER_TABLE** devem ser feitas na mão e colocadas nesse mesmo arquivo, assim como a declaraçao de todas as functions que serão utilizadas, para cada tabela.   
 
 
### GraphQL
Para as requisições ao GraphQL, é possivel usar a lib multiplatform do apollo : [Apollo Multiplatform](https://www.apollographql.com/docs/android/essentials/get-started-multiplatform/) Porém a mesma ainda em fase experimental.    

A lib se mostrou bem eficaz, e uma mutation de login foi feita com sucesso em ambas as plataformas.      

- Vantagens: 
1) Mesmo modelo ja feito hoje, executando as mutations e retornando um Flow<Response> com bastante tranquilidade. 
2) Schema gerado da mesma maneira que o de sempre assim como as classes.   

- Desvantagens: Ainda em experimental, é necessario testar o upload de documentos em ambas as plataformas.   

### Injeção de dependencia:
- [Koin Multiplatform](https://insert-koin.io/docs/setup/v3)
Com a mesma estrutura de como é no Android com o Koin JVM, toda a parte de repository/usecase/datasource foi injetada sem problemas
Pra iOS foi necessario apenas um passo a mais para que fosse identificado 

### Flow
Toda implementaçao das camadas deverao retornar objetos flow ou common flow para o acesso de ambos as plataformas - maior parte dos usecases de Android já estao nesse formato, o que nao seria tanto desenvolvimento
Será necessaria uma re-implentaçao dos viewmodels para coletar os flows - iOS apenas
 

