package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> onSignInResult(result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se já estiver logado, vai direto para a tela principal
        if (FirebaseAuth.getInstance().currentUser != null) {
            irParaMain()
            return
        }

        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.btnEntrar).setOnClickListener { iniciarLogin() }
    }

    private fun iniciarLogin() {
        // O provedor de e-mail já inclui a opção de cadastrar novo usuário
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            Log.d("FIREBASE", "login com sucesso")
            irParaMain()
        } else {
            val erro = result.idpResponse?.error
            Log.d("FIREBASE", "LOGIN INCORRETO: $erro")
            if (erro != null) {
                Toast.makeText(this, "Erro ao entrar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irParaMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}