package com.ismealdi.hidoc.view.user.conversation

import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.struct.Chat
import com.ismealdi.hidoc.struct.Conversation
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.ACTIVITY.USER
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.view.user.conversation.adapter.ConversationAdapter
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.component_toolbar.*

class ConversationActivity : AmActivity(R.layout.activity_conversation), ConversationContract.View {

    override var presenter: ConversationContract.Presenter? = ConversationPresenter(this, this)

    private var user: User? = null
    private var chat: Chat? = null

    private var conversationAdapter: ConversationAdapter? = null

    override fun initView() {
        initToolbar(back = true)

        buttonSend.isEnabled = false
        inputConversation.isEnabled = false

        user = intent.getParcelableExtra(USER)

        user?.let {
            presenter?.chat(it)
            setPageName(it.fullName, false)
            setTitleGravity(Gravity.CENTER)

            imageProfilePicture.visibility = View.VISIBLE
            Utils().imageCircle(imageProfilePicture, it.photoUrl, this)
        }

        conversationAdapter = ConversationAdapter(mutableListOf(), this)

        listConversations.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        listConversations.adapter = conversationAdapter
    }

    override fun displayConversation(conversation: Conversation) {
        conversationAdapter?.add(conversation)
        listConversations?.smoothScrollToPosition(conversationAdapter?.itemCount ?: 0)
    }

    override fun bindChat(chat: Chat) {
        this.chat = chat
        buttonSend.isEnabled = true
        inputConversation.isEnabled = true
        presenter?.conversation(chat.uid)
    }

    override fun initListener() {
        super.initListener()

        buttonSend.setOnClickListener {_ ->
            messageSend()
        }
    }

    private fun messageSend() {
        user?.let { to ->
            val message = inputConversation.text.toString()

            if (message.isNotBlank()) {
                chat?.let {
                    presenter?.send(to = to, message = message, chat = it)
                    inputConversation.setText("")
                }
            }
        }
    }

}