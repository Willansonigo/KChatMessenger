package br.disciplina.kchatmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_messages)

        verifyAuthentication()

    }

    private fun verifyAuthentication() {
        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(this@MessagesActivity, LoginActivity::class.java) // Se você apertar o botão de voltar do smartphone, ao invés de exibir novamente a tela de criar conta, iremos fechar o aplicativo - porque ele já está logado.
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // algumas flags para indicar que esta nova activity está no topo de todas as outras.
            startActivity(intent)


        }

    }

         override fun onCreateOptionsMenu(menu: Menu):Boolean {
            menuInflater.inflate(R.menu.message_menu, menu)
            return true

        }

         override fun onOptionsItemSelected(item: MenuItem):Boolean { // Método responsável por observar eventos de clicks nos menus
            when (item?.itemId) { // Quando o ID do item - menu - for selecionado, faça:
                R.id.logout -> { // Identifica se o evento de click foi gerado pelo menu logout
                    FirebaseAuth.getInstance().signOut() // Efetua o logout no Firebase
                    verifyAuthentication() // Verifica novamente qual activity deve ser exibida - neste caso, a de Login
                }
                R.id.contacts -> { // Caso click no menu de contatos, por enquanto apenas faça o Log no console
                    Log.i("Teste", "contacts clicked")
                    val intent = Intent(this@MessagesActivity, ContactsActivity::class.java)
                    startActivity(intent)
                }

            }
             return super.onOptionsItemSelected(item)
        }



    }
