package dev.decagon.networkingclass.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import dev.decagon.networkingclass.App
import dev.decagon.networkingclass.R
import dev.decagon.networkingclass.model.request.UserDataRequest
import dev.decagon.networkingclass.network.NetworkStatusChecker

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signInButton: Button
    private lateinit var errorText: TextView
    private lateinit var loading: ProgressBar

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    private val remoteApi = App.remoteApi
    private val networkStatusChecker by lazy {
        NetworkStatusChecker(getSystemService(ConnectivityManager::class.java)) {
//            Snackbar.make(
//                signInButton,
//                "No internet connection! Please turn on your internet and try again.",
//                Snackbar.LENGTH_LONG
//            ).show()
            showLoginError("No internet connection! Please turn on your internet and try again.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUi()

        if (App.getToken().isNotBlank()) {
            startActivity(MainActivity.getIntent(this))
        }
    }

    private fun initUi() {
        title = "Sign In"
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)
        signInButton = findViewById(R.id.sign_in)
        errorText = findViewById(R.id.error_text)
        loading = findViewById(R.id.loading)

        signInButton.setOnClickListener {
            loading.visibility = View.VISIBLE
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                signInUser(UserDataRequest(username, password))
            } else {
                showLoginError(null)
                loading.visibility = View.GONE
            }
        }
    }

    private fun showLoginError(message: String?) {
        errorText.visibility = View.VISIBLE
        loading.visibility = View.GONE
        message?.let { errorText.text = it }
    }

    private fun signInUser(userDataRequest: UserDataRequest) {
        remoteApi.signInUser(
            userDataRequest = userDataRequest,
            onError = ::showLoginError
        ) {
            errorText.visibility = View.GONE
            loading.visibility = View.GONE
            App.saveToken(it)
            startActivity(MainActivity.getIntent(this))
        }
    }
}