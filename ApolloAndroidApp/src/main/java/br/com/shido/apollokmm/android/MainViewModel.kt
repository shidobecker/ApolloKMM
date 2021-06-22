package br.com.shido.apollokmm.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.shido.apollokmm.android.BackgroundDispatcher.Background
import br.com.shido.apollokmm.loginusecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val useCase: LoginUseCase) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState<Boolean>>(LoginUiState.Loading())
    val loginState: StateFlow<LoginUiState<Boolean>> get() = _loginState

    fun login(username: String, password: String) {
    /*    viewModelScope.launch {
            useCase.fetchLogin(username, password)
                .flowOn(Dispatchers.Background)
                .shareIn(
                    scope = this,
                    replay = 1,
                    started = SharingStarted.WhileSubscribed()
                )
                .collect {
                    _loginState.value = LoginUiState.Success(true)
                }

        }*/

        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading()

            try{
                useCase.fetchLogin(username, password)
                    .flowOn(Dispatchers.Background)
                    .catch {
                        _loginState.value = LoginUiState.Error(it)
                    }
                    .collect {
                        _loginState.value = LoginUiState.Success(true)
                    }
            }catch (e: Exception){
                e.printStackTrace()
                _loginState.value = LoginUiState.Error(e)

            }


        }
    }


}


sealed class LoginUiState<T> {
    data class Success<T>(val data: T) : LoginUiState<T>()
    data class Error<T>(val exception: Throwable) : LoginUiState<T>()
    class Loading<T> : LoginUiState<T>()
}