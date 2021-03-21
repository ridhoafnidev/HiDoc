package com.ismealdi.hidoc.view.user.conversation

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentChange
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.struct.Chat
import com.ismealdi.hidoc.struct.Conversation
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.extensions.chat
import com.ismealdi.hidoc.utils.extensions.conversation
import com.ismealdi.hidoc.utils.extensions.user

class ConversationPresenter(private val view: ConversationContract.View, private val context: Context) : ConversationContract.Presenter {

    init {
        view.presenter = this
    }

    private var check = 0

    private val onFailureListener = OnFailureListener {
        view.onError(it.message.toString())
    }

    override fun send(to: User, message: String, chat: Chat) {
        App.amAuth.currentUser?.let { current ->

            App.amDatabase.user(current.uid).get().addOnSuccessListener {
                it.toObject(User::class.java)?.let { user ->

                    val conversation = Conversation(chat = chat.uid, fromName = user.fullName, fromToken = user.pushId, message = message,
                        toToken = to.pushId, fromUid = current.uid)

                    addConversation(conversation)
                }
            }

        }
    }

    override fun conversation(uid: String) {
        App.amDatabase.conversation().whereEqualTo(Conversation.Companion.Fields.chat, uid).addSnapshotListener {
                querySnapshot, error ->
            error?.let {
                view.onError(it.message.toString())
                return@addSnapshotListener
            }

            querySnapshot?.let {
                it.documentChanges.forEach { document ->
                    if(document.type == DocumentChange.Type.ADDED) {
                        view.displayConversation(document.document.toObject(Conversation::class.java))
                    }
                }
            }

        }
    }

    private fun addConversation(conversation: Conversation) {
        App.amDatabase.conversation().add(conversation).addOnSuccessListener {
            updateLatestChat(conversation)
        }.addOnFailureListener(onFailureListener)
    }

    private fun updateLatestChat(conversation: Conversation) {
        App.amDatabase.chat(conversation.chat).get().addOnSuccessListener {
            it.toObject(Chat::class.java)?.let {
                val chat = it
                chat.lastMessage = conversation.message
                chat.lastSendOn = conversation.createdOn

                App.amDatabase.chat(conversation.chat).set(chat).addOnFailureListener(onFailureListener)
            }
        }.addOnFailureListener(onFailureListener)
    }

    override fun chat(user: User) {
        App.amAuth.currentUser?.let { currentUser ->
            val chat = Chat(owner = currentUser.uid, friend = user.uid)

            App.amDatabase.chat()
                .whereEqualTo(Chat.Companion.Fields.owner, chat.owner)
                .whereEqualTo(Chat.Companion.Fields.friend, chat.friend)
                .limit(1).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { query ->
                            if(query.isEmpty) {
                                check++
                                checkOther(chat.friend, chat.owner)
                            }else{
                                query.documents.forEach {
                                    it.toObject(Chat::class.java)?.let {chatResult ->
                                        view.bindChat(chatResult)
                                    }
                                }
                            }
                        }
                    }

                }
        }
    }

    private fun checkOther(owner: String, friend: String) {
        App.amDatabase.chat()
            .whereEqualTo(Chat.Companion.Fields.owner, owner)
            .whereEqualTo(Chat.Companion.Fields.friend, friend)
            .limit(1).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { query ->
                        if(query.isEmpty) {
                            check++

                            if(check == 2) {
                                addChat(Chat(owner = owner, friend = friend))
                            }
                        }else{
                            query.documents.forEach {
                                it.toObject(Chat::class.java)?.let {chatResult ->
                                    view.bindChat(chatResult)
                                }
                            }
                        }
                    }
                }

            }

    }

    private fun addChat(chat: Chat) {
        App.amDatabase.chat().add(chat).addOnSuccessListener {
            updateChat(it.id, chat)
        }.addOnFailureListener {
            view.onError(it.message.toString())
        }
    }

    private fun updateChat(id: String, chat: Chat) {
        chat.uid = id

        App.amDatabase.chat(id).set(chat).addOnSuccessListener {
            view.bindChat(chat)
        }.addOnFailureListener {
            view.onError(it.message.toString())
        }
    }


}