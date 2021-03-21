package com.ismealdi.hidoc.view.user.conversation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.Conversation
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.components.AmTextView
import com.ismealdi.hidoc.utils.extensions.toTime
import kotlinx.android.synthetic.main.item_conversation.view.*

class ConversationAdapter(private var data: MutableList<Conversation>, private val context: Context) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val messagePrimary: AmTextView = itemView.labelMessagePrimary
		val timePrimary: AmTextView = itemView.labelTimePrimary
		val messageSecondary: AmTextView = itemView.labelMessageSecondary
		val timeSecondary: AmTextView = itemView.labelTimeSecondary
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
		return ViewHolder(view)
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val conversation = data[position]

		val time = conversation.createdOn.toDate().toTime()

		if(Preferences(context).getUid() == conversation.fromUid) {
			holder.messagePrimary.text = conversation.message
			holder.timePrimary.text = time
			holder.timePrimary.visibility = View.VISIBLE
			holder.messagePrimary.visibility = View.VISIBLE
			holder.timeSecondary.visibility = View.GONE
			holder.messageSecondary.visibility = View.GONE
		}else{
			holder.messageSecondary.text = conversation.message
			holder.timeSecondary.text = time
			holder.timeSecondary.visibility = View.VISIBLE
			holder.messageSecondary.visibility = View.VISIBLE
			holder.timePrimary.visibility = View.GONE
			holder.messagePrimary.visibility = View.GONE
		}

	}

	override fun getItemCount(): Int {
		return data.size
	}

	fun update(list: List<Conversation>) {
        this.data.clear()
        this.data.addAll(list)

		this.notifyDataSetChanged()
	}

	fun add(conversation: Conversation) {
        this.data.add(conversation)

		this.notifyItemInserted(this.itemCount)
	}
}