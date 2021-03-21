package com.ismealdi.hidoc.view.user.home.adapter.doctor

import com.ismealdi.hidoc.struct.User

interface DoctorAdapterInterface {

    fun onPlusClick(position: Int, user: User)

    fun onChatClick(position: Int, user: User)

    fun onDoctorClick(user: User)

}