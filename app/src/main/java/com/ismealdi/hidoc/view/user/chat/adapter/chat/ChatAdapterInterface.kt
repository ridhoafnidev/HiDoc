package com.ismealdi.hidoc.view.user.chat.adapter.chat

import com.ismealdi.hidoc.struct.User


interface ChatAdapterInterface {

    fun onItemClick(position: Int, user: User)

}