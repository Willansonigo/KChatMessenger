package br.disciplina.kchatmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.act_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        btn_enter.setOnClickListener {
            signIn()
        }


        txt_account.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // algumas flags para indicar que esta nova activity está no topo de todas as outras.
            startActivity(intent)
        }
    }

    private fun signIn() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString() // text ele pega o texto da tela e 'toString' ele volta para ser string para ir para a variável.

        Log.i("Login", "Attempt login with email/pw: $email/***")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"email e senha deve ser informados", Toast.LENGTH_LONG).show()

            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Log.i("Test", it.result?.user!!.uid)
                val intent = Intent(this@LoginActivity, MessagesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.e("Teste", it.message, it)
            }

    }
}