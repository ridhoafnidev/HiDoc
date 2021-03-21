package com.ismealdi.hidoc.view.user.detail

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.User

interface UserDetailContract {

    interface View : AmView<Presenter> {

        fun displayUserInformation(user: User)

    }

    interface Presenter : AmPresenter {


    }

}