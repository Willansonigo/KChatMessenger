package br.disciplina.kchatmessenger

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.act_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private var mSelectedUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // adicionar Layout
        setContentView(R.layout.act_register)

        btn_register.setOnClickListener {
            createUser()
        }

        btn_selected_photo.setOnClickListener {
            selectPhoto()
        }

        already_have_account_txt_view.setOnClickListener {
            Log.d("MainActiviy", "try to show login activity")
            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }



    }

    private fun selectPhoto() {

        val intent = Intent(Intent.ACTION_PICK)// 1 - Define o tipo de intenção. Desta vez não vamos abrir uma activity e sim uma ação de selecionar arquivos da galeria.
        intent.type = "image/*" // 2 - Define o tipo de arquivo que podemos selecionar - somente imagem.
        startActivityForResult(intent, 0) // 3 - Inicia um método que é disparado quando a activity volta ficar visível. Esse método é o: método a baixo.

    }

    //var selectedPhotoUri: Uri? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // Isso é para pegar o que vai retornar a foto do smartphone.
        super.onActivityResult(requestCode, resultCode, data)


        /*  if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the select image was...
                Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data // get access to image

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            btn_selected_photo.setBackgroundDrawable(bitmapDrawable)*/


        if (requestCode == 0) {
            mSelectedUri = data?.data // Desta forma conseguimos buscar a URI de onde se encontra a foto dentro do nosso Smartphone.
            Log.i("Teste", mSelectedUri.toString())
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap =
                        MediaStore.Images.Media.getBitmap( // E o código-fonte deverá transformar a URI em um objeto Bitmap para ser adicionado nesse componente CircleImageView.
                                contentResolver,
                                mSelectedUri
                        )
                img_photo.setImageBitmap(bitmap)
                btn_selected_photo.alpha = 0f
            }
            btn_selected_photo.alpha =
                    0f // Por fim, escondemos (em tese) o botão ao diminuir suas cores a transparência com alpha = 0f.
        }


    }


    private fun createUser() { //fun indica um método ou função.
        //val nome = edit_name_register.text.toString()
        val email = edit_email_resgister.text.toString() //text ta pegando o texto que ta buscando a variável.
        val password = edit_password_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "email e senha devem, ser informados", Toast.LENGTH_LONG).show()

            return
        }

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.i("Teste", "UserID é ${it.result?.user?.uid}")

                        saveUserInFirebase(); // 1. Adicionamos um evento de salvar a foto após o usuário ser autenticado
                    }


                }.addOnFailureListener {
                    Log.i("Main","Falha ao criar usuário: ${it.message}")
                    Toast.makeText(this, "Falha ao criar usuário: ${it.message}", Toast.LENGTH_LONG).show()
                }

    }

    private fun saveUserInFirebase() {

        val filename = UUID.randomUUID().toString() // 2. Criamos um nome de arquivo único através dos método UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance()
                .getReference("/images/${filename}") // 3. Criamos um nó (diretório/caminho) dentro do banco de storage onde será salva na pasta /images/ o nome do arquivo.

        mSelectedUri?.let { // 4. Garantimos através do let que o bloco seguinte somente será executado SE a variável realmente tiver uma referência de URI, ou seja, ela não estará nula.
            mSelectedUri
            ref.putFile(it) // 5. Colocamos o objeto atual it que é nossa URI na referência para que ele seja feito o upload. É aqui que acontece o upload para o firebase. O Android irá verificar onde se encontra o arquivo no smartphone pela URI e irá subir o arquivo de imagem ao firebase.
                    .addOnSuccessListener {// // 6. Declaramos o evento de Listener de sucesso para conseguirmos buscar a foto lá no  Storage do Firebase.
                        ref.downloadUrl.addOnSuccessListener {// // 7. Executamos o download da imagem (URL) e imprimimos no console a URL oficial já fornecida pelo Firebase.
                            Log.i("Teste", it.toString())

                            saveUserInFirebase()

                            val url = it.toString() // 1. Obtem no formato String a url da foto.
                            val name = edit_name_register.text.toString() // 2. Obtem o nome digitado pelo usuário.
                            val uid = FirebaseAuth.getInstance().uid!! // 3. Obtem o UID do usuário autenticado.

                            val user = User(uid, name, url) // 4. Cria um objeto User a a partir do modelo de classe data class User.

                            FirebaseFirestore.getInstance().collection("users") // 5. Constrói uma referência a nossa primeira coleção: users.
                                    .add(user) // 6. Efetiva a inserção de um novo usuário no firebase.
                                    //.document(uid)
                                    // .set(user)
                                    .addOnSuccessListener { // 7. Adiciona eventos de sucesso ao criar o novo documento.
                                        Log.i("Teste", it.toString())
                                        // val intent = Intent(this@RegisterActivity, MessagesActivity::class.java)
                                        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        //startActivity(intent)
                                        // }
                                        //  .addOnFailureListener { // 8. Adiciona eventos de falha ao criar o novo documento.
                                        //    Log.e("Teste", it.message, it)
                                        // }
                                    }
                        }

                    }
        }
    }



}

    //private fun saveUserToFirebaseDatabase() {

        //val uid = FirebaseAuth.getInstance().uid
        //FirebaseDatabase.getInstance().getReference("/users/$uid")

 // data class User(val uid: String, val name: String, val url: String) // são classes com objetivo de somente espaços de armazenamento de variáveis. Por isso o nome data class, uma classe para armazenar a URL da foto.
// No passo anterior, já tinhamos a URL da foto, agora vamos criar um novo usuário com o Objeto User e atribuir essa URL a ele. Depois, salvar no banco de dados do Firebase.







