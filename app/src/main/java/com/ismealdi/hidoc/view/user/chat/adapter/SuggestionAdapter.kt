package com.ismealdi.hidoc.view.user.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.components.AmTextView
import com.ismealdi.hidoc.view.user.chat.adapter.chat.ChatAdapterInterface
import kotlinx.android.synthetic.main.item_suggestion.view.*

/**
 * Created by Al
 * on 07/04/19 | 23:42
 */
class SuggestionAdapter(private var data: MutableList<User>, private val context: Context, private val listener: ChatAdapterInterface) : RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.imageProfilePicture
        val name: AmTextView = itemView.labelName
        val container: ConstraintLayout = itemView.layoutContainer
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = data[position]

        Utils().imageCircle(holder.image, user.photoUrl, context)
        holder.name.text = String.format(context.getString(R.string.text_dr), user.fullName)

        holder.container.setOnClickListener {
            listener.onItemClick(position, user)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun add(user: User) {
        this.data.add(user)

        this.notifyItemInserted(this.itemCount)
    }

    fun remove(user: User) {
        val index = this.data.indexOf(this.data.find { it.uid == user.uid})

        if(index > -1) {
            this.data.removeAt(index)
            this.notifyItemRemoved(index)
        }
    }
}