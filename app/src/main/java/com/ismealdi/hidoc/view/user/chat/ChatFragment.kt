package com.ismealdi.hidoc.view.user.chat

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmFragment
import com.ismealdi.hidoc.struct.Chat
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.view.user.chat.adapter.chat.ChatAdapter
import com.ismealdi.hidoc.view.user.chat.adapter.chat.ChatAdapterInterface
import com.ismealdi.hidoc.view.user.chat.adapter.SuggestionAdapter
import com.ismealdi.hidoc.view.user.conversation.ConversationActivity
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : AmFragment(R.layout.fragment_chat, R.string.title_chat), ChatContract.View,
	ChatAdapterInterface {

	override var presenter: ChatContract.Presenter? = null

	private var suggestionAdapter: SuggestionAdapter? = null
	private var chatAdapter: ChatAdapter? = null
	
	override fun initView() {
		amActivity?.let {
			presenter = ChatPresenter(this, it)
			initAdapter(it)	
			initLists(it)

            presenter?.active()
            presenter?.users()
            presenter?.chats()
		}
	}

	private fun initAdapter(context: Context) {
		suggestionAdapter = SuggestionAdapter(mutableListOf(), context, this)
        chatAdapter =
			ChatAdapter(mutableListOf(), hashMapOf(), context, this)
	}
    
    private fun initLists(context: Context) {
        gridSuggestion.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        gridSuggestion.adapter = suggestionAdapter
        
        listChat.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        listChat.adapter = chatAdapter
    }

	override fun displayRecent(chat: Chat) {
		chatAdapter?.add(chat)
	}

	override fun updateRecent(chat: Chat) {
		chatAdapter?.modified(chat)
	}

	override fun displayUser(users: User) {
		chatAdapter?.updateUser(users)
	}

	override fun displayActive(user: User) {
		suggestionAdapter?.add(user)
		if(suggestionAdapter?.itemCount ?: 0 > 0) {
			gridSuggestion.visibility = View.VISIBLE
		}
	}

	override fun updateActive(user: User) {
		suggestionAdapter?.remove(user)
		if(suggestionAdapter?.itemCount == 0) {
			gridSuggestion.visibility = View.GONE
		}
	}

	override fun onItemClick(position: Int, user: User) {
		activity?.let {
			val intent = Intent(it, ConversationActivity::class.java)
			intent.putExtra(Constants.INTENT.ACTIVITY.USER, user)
			startActivity(intent)
		}
	}

}