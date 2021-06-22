package br.com.shido.apollokmm

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}