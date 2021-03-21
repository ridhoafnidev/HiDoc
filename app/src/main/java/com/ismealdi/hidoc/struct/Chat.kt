package com.ismealdi.hidoc.struct

import com.google.firebase.Timestamp

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class Chat(
		var uid: String = "",
		var owner: String = "",
		var friend: String = "",
		var lastMessage: String = "",
		var lastSendOn: Timestamp = Timestamp.now(),
		var isReaded: Boolean = false,
		var createdOn: Timestamp = Timestamp.now()
) {

	companion object {
		const val tableName = "chats"

		object Fields {
			const val uid = "uid"
			const val owner = "owner"
			const val friend = "friend"
			const val lastMessage = "lastMessage"
			const val lastSendOn = "lastSendOn"
			const val isReaded = "isReaded"
			const val createdOn = "createdOn"
		}
	}

}