package br.com.shido.apollokmm.android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.shido.apollokmm.android.databinding.ActivityMainBinding
import br.com.shido.apollokmm.android.utils.hide
import br.com.shido.apollokmm.android.utils.show
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private val binding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
    }

    private fun setupViews() {
        setupButton()

    }


    suspend fun <T> StateFlow<LoginUiState<T>>.collectLatestData(
        onSuccess: (data: T) -> Unit,
        onLoading: () -> Unit = { }, onError: () -> Unit = {}
    ) {
        collectLatest { state ->
            when (state) {
                is LoginUiState.Loading -> onLoading()
                is LoginUiState.Error -> onError()
                is LoginUiState.Success -> onSuccess(state.data)
            }
        }

    }


    private fun initObserveLoginState() {

        lifecycleScope.launch {

            viewModel.loginState.collectLatestData(onError =
            ::showError, onLoading =
            ::showLoadingAndLockViews, onSuccess = {
                showSuccess(it)
            })
        }
    }

    private fun setupButton() {
        with(binding) {
            button.setOnClickListener {
                viewModel.login(username.text.toString(), password.text.toString())
                initObserveLoginState()
            }
        }
    }


    private fun showSuccess(data: Boolean) {
        hideLoadingAndUnlockViews()
        Toast.makeText(this@MainActivity, "Success $data", Toast.LENGTH_LONG)
            .show()
    }

    private fun showError() {
        Toast.makeText(this@MainActivity, "Um erro ocorreu", Toast.LENGTH_LONG)
            .show()

        hideLoadingAndUnlockViews()
    }

    private fun showLoadingAndLockViews() {
        with(binding) {
            progress.show()
            button.hide()
            username.isEnabled = false
            password.isEnabled = false
        }
    }

    private fun hideLoadingAndUnlockViews() {
        with(binding) {
            progress.hide()
            button.show()
            username.isEnabled = true
            password.isEnabled = true
        }
    }


}
