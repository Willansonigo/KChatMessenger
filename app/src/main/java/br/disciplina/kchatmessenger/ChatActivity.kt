package br.disciplina.kchatmessenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.act_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var mAdapter: GroupAdapter<ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_chat)

     //   val user = intent.extras?.getParcelable<User>(ContactsActivity.USER_KEY)
      //  Log.i("Teste", "username ${user?.name}")

     //   supportActionBar?.title = user?.name


        mAdapter = GroupAdapter()
        recycler_chat.adapter = mAdapter

        //mAdapter.add(ChatActivity)
        mAdapter.add(MessageItem(true))
        mAdapter.add(MessageItem(false))
        mAdapter.add(MessageItem(false))
        mAdapter.add(MessageItem(true))
        mAdapter.add(MessageItem(true))
        mAdapter.add(MessageItem(false))

    }

    private inner class MessageItem (private val mleft: Boolean) : Item<ViewHolder>() {

            override fun getLayout():Int {
                return if (mleft) R.layout.item_from_message
                else R.layout.item_to_message
            }

        override fun bind(vieHolder: ViewHolder, position: Int) {

        }
        }
}