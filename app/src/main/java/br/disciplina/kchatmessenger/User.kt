package br.disciplina.kchatmessenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String = "", val name: String = "", val url: String = ""):Parcelable { //Desta forma, o Firebase consegue instanciar um novo objeto User usando um construtor padrão (sem parâmetros).









    //data class user(val uid: String = "",
              //      val name: String = "",
               //     val url: String = "")





     // são classes com objetivo de somente espaços de armazenamento de variáveis. Por isso o nome data class, uma classe para armazenar a URL da foto.
// No passo anterior, já tinhamos a URL da foto, agora vamos criar um novo usuário com o Objeto User e atribuir essa URL a ele. Depois, salvar no banco de dados do Firebase.




}