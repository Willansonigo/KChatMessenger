package br.disciplina.kchatmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.act_contacts.*
import kotlinx.android.synthetic.main.item_user.view.*

class ContactsActivity : AppCompatActivity() {

    private lateinit var mAdapter: GroupAdapter<ViewHolder>

    companion object {
        val USER_KEY = "user_key"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_contacts)

        mAdapter = GroupAdapter()
        recycler.adapter = mAdapter // Esse recycler é o id do recycler.view que ta no layout de act_contacts.xml.

        // Cria evento para abrir a tela de chat (conversas)
        mAdapter.setOnItemClickListener { item, view ->
            val intent = Intent(this@ContactsActivity, ChatActivity::class.java)
            val userItem: UserItem = item as UserItem

            intent.putExtra(USER_KEY, userItem.mUser)
            startActivity(intent)
        }

        fetchUsers()
    }

     private inner class UserItem(val mUser: User) //  atribuir cada usuário em uma linha da lista
        : Item<ViewHolder>() { // Declara a classe responsável por gerenciar uma linha da lista. Essa classe recebe em seu construtor um usuário e ela herda as propriedades da class Item<ViewHolder>

            override fun getLayout(): Int { // Define qual será o layout de cada linha.
                return R.layout.item_user
            }

            override fun bind(viewHolder: ViewHolder, position: Int) { // Esse método "conecta" os componentes dessa nova view e atribui os dados sempre que ela for exibida.
                viewHolder.itemView.txt_username.text = mUser.name // Atribui o nome do usuário no campo de texto
                Picasso.get()
                        .load(mUser.url)
                        .into(viewHolder.itemView.img_photo)

            }
        }

    private fun fetchUsers() { //Essa bloco de código se conectar no Firestore e busca todos os usuários cadastrados.
        FirebaseFirestore.getInstance().collection("/users/") // Criamos uma referência para o nó(coleção) chamada users.
            .addSnapshotListener { snapshot, exception ->
                exception?.let { //Verificamos se ocorreu algum erro no Listener e na chamada do Firebase. Caso ocorra, exibimos no Log.
                    Log.d("Teste" ,it.message.toString())
                    return@addSnapshotListener
                }

                snapshot?.let { // Verificamos se há um objeto com a lista de usuários(documentos).
                    for (doc in snapshot) { // Busca cada documento.
                        //val user = doc.toObject(User::class.java) //Transforma um documento em um objeto tipado para podermos manipular - no nosso caso, User.
                        val user = doc.toObject(User::class.java)
                        Log.i("Teste", "user ${user.uid}, ${user.name}")
                        mAdapter.add(UserItem(user))

                    }
                }
            }

    }
}