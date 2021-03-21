package com.ismealdi.hidoc.view.user.chat.adapter.chat

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.Chat
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.components.AmTextView
import com.ismealdi.hidoc.utils.extensions.toTime
import kotlinx.android.synthetic.main.item_chat.view.*

/**
 * Created by Al
 * on 07/04/19 | 23:47
 */
class ChatAdapter(private var data: MutableList<Chat>, private var users: HashMap<String, User>, private val context: Context, private val listener: ChatAdapterInterface) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.imageProfilePicture
        val name: AmTextView = itemView.labelName
        val message: AmTextView = itemView.labelMessage
        val date: AmTextView = itemView.labelDate
        val container: ConstraintLayout = itemView.layoutContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = data[position]
        var user : User = User()

        if(conversation.owner == Preferences(context).getUid()) {
            users[conversation.friend]?.let {
                user = it
            }
        }else {
            users[conversation.owner]?.let {
                user = it
            }
        }

        Utils().imageCircle(holder.image, user.photoUrl, context)

        holder.name.text = user.fullName
        holder.message.text = conversation.lastMessage
        holder.date.text = conversation.lastSendOn.toDate().toTime()

        holder.container.setOnClickListener {
            listener.onItemClick(position, user)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateUser(user: User) {
        this.users[user.uid] = user
    }

    fun add(chat: Chat) {
        this.data.add(chat)

        this.notifyItemInserted(this.itemCount)
    }

    fun modified(chat: Chat) {
        val index = this.data.indexOf(this.data.find { it.uid == chat.uid})

        if(index > -1) {
            this.data[index] = chat
            this.notifyItemChanged(index)
        }
    }
}