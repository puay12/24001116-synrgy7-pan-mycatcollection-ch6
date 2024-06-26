package com.example.mycatcollections.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mycatcollections.databinding.ActivityLoginBinding
import com.example.mycatcollections.presentation.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.buttonLogin.setOnClickListener {
            if (binding.usernameInput.text.isNullOrEmpty()) {
                binding.usernameInput.error = "Username harus diisi"
            } else if (binding.passwordInput.text.isNullOrEmpty()) {
                binding.passwordInput.error = "Password harus diisi"
            } else {
                binding.usernameInput.error = null
                binding.passwordInput.error = null

                viewModel.userLogin(
                    username = binding.usernameInput.text.toString(),
                    password = binding.passwordInput.text.toString()
                )
            }
        }

        binding.buttonCrash.setOnClickListener { throw RuntimeException("Test Crash") }

        viewModel.getLoading().observe(this) { isLoading ->
            if (isLoading) {
                binding.flipperLogin.displayedChild = 1
            } else {
                binding.flipperLogin.displayedChild = 0
            }
        }

        viewModel.getSuccess().observe(this) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(
                    binding.root,
                    "Login berhasil!",
                    Snackbar.LENGTH_LONG
                ).show()
                MainActivity.startActivity(this)
            }
        }

        viewModel.getError().observe(this) { throwable ->
            Snackbar.make(
                binding.root,
                throwable.message.toString(),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}