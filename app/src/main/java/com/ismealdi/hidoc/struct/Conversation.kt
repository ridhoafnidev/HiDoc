package com.ismealdi.hidoc.struct

import com.google.firebase.Timestamp

/**
 * Created by Al
 * on 07/04/19 | 01:29
 */
class Conversation(
        var chat: String = "",
        var message: String = "",
        var state: Int = 0,
        val toToken: String = "",
        val fromUid: String = "",
        val fromToken: String = "",
        val fromName: String = "",
        var createdOn: Timestamp = Timestamp.now(),
        var readOn: Timestamp = Timestamp.now()
) {

        companion object {
                const val tableName = "conversations"

                object Fields {
                        const val chat = "chat"
                        const val state = "state"
                        const val toToken = "toToken"
                        const val fromUid = "fromUid"
                        const val fromToken = "fromToken"
                        const val fromName = "fromName"
                        const val createdOn = "createdOn"
                        const val readOn = "readOn"
                }
        }
}
