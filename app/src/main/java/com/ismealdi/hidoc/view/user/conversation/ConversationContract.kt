package com.ismealdi.hidoc.view.user.conversation

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.Chat
import com.ismealdi.hidoc.struct.Conversation
import com.ismealdi.hidoc.struct.User

interface ConversationContract {

    interface View : AmView<Presenter> {

        fun displayConversation(conversation: Conversation)
        fun bindChat(chat: Chat)

    }

    interface Presenter : AmPresenter {

        fun send(to: User, message: String, chat: Chat)
        fun chat(user: User)
        fun conversation(uid: String)

    }

}