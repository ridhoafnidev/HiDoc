package com.ismealdi.hidoc.view.user.profile

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.User

interface ProfileContract {

    interface View : AmView<Presenter> {
        fun showSignIn()
        fun displayUserInfo(user: User)
    }

    interface Presenter : AmPresenter {
        fun signOut()
        fun user()
    }

}