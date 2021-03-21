package com.ismealdi.hidoc.view.user.chat

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentChange
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.struct.Chat
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.extensions.chat
import com.ismealdi.hidoc.utils.extensions.user

class ChatPresenter(private val view: ChatContract.View, private val context: Context) : ChatContract.Presenter {
	
	init {
		view.presenter = this
	}

	private val onFailureListener = OnFailureListener {
		view.onError(it.message.toString())
	}

	override fun chats() {
		App.amDatabase.chat().whereEqualTo(Chat.Companion.Fields.owner, Preferences(context).getUid()).addSnapshotListener {
				querySnapshot, error ->
			error?.let {
				view.onError(it.message.toString())
				return@addSnapshotListener
			}

			querySnapshot?.let {
				it.documentChanges.forEach { document ->
					val chat = document.document.toObject(Chat::class.java)
					if(document.type == DocumentChange.Type.ADDED) {
						view.displayRecent(chat)
					}else if(document.type == DocumentChange.Type.MODIFIED) {
						view.updateRecent(chat)
					}
				}
			}

		}

		others()
	}

	private fun others() {
		App.amDatabase.chat().whereEqualTo(Chat.Companion.Fields.friend, Preferences(context).getUid()).addSnapshotListener {
				querySnapshot, error ->
			error?.let {
				view.onError(it.message.toString())
				return@addSnapshotListener
			}

			querySnapshot?.let {
				it.documentChanges.forEach { document ->
					val chat = document.document.toObject(Chat::class.java)
					if(document.type == DocumentChange.Type.ADDED) {
						view.displayRecent(chat)
					}else if(document.type == DocumentChange.Type.MODIFIED) {
						view.updateRecent(chat)
					}
				}
			}

		}
	}

	override fun users() {
		App.amDatabase.user().addSnapshotListener {
				querySnapshot, error ->
			error?.let {
				view.onError(it.message.toString())
				return@addSnapshotListener
			}

			querySnapshot?.let {
				it.documentChanges.forEach { document ->
					val user = document.document.toObject(User::class.java)
					if(document.type == DocumentChange.Type.ADDED && Preferences(context).getUid() != user.uid) {
						view.displayUser(user)
					}
				}
			}

		}
	}

	override fun active() {
		App.amDatabase.user().whereEqualTo(User.CREATOR.Fields.onlineUser, true).addSnapshotListener {
				querySnapshot, error ->
			error?.let {
				view.onError(it.message.toString())
				return@addSnapshotListener
			}

			querySnapshot?.let {
				it.documentChanges.forEach { document ->
					val user = document.document.toObject(User::class.java)

					if(Preferences(context).getUid() != user.uid) {
						if(document.type == DocumentChange.Type.ADDED) {
							view.displayActive(user)
						}else if(document.type == DocumentChange.Type.REMOVED) {
							view.updateActive(user)
						}
					}
				}
			}

		}
	}
}