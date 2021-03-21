package com.ismealdi.hidoc.struct

import com.google.firebase.Timestamp

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class Favorite(
	var owner: String = "",
	var friend: String = "",
	var toToken: String = "",
	var toName: String = "",
	var createdOn: Timestamp = Timestamp.now()
) {

	companion object {
		const val tableName = "favorites"

		object Fields {
			const val owner = "owner"
			const val friend = "friend"
			const val toToken = "toToken"
			const val toName = "toName"
			const val createdOn = "createdOn"
		}
	}

}