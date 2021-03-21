package com.ismealdi.hidoc.view.auth.signup

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.utils.interfaces.AmNetworkInterface

/**
 * Created by Al
 * on 20/04/19 | 02:08
 */
interface SignUpContract {
    interface View : AmView<Presenter>, AmNetworkInterface {
        fun showMain()
    }

    interface Presenter : AmPresenter {
        fun checkSession()
        fun validate(fullName: String, email: String, password: String, phone: String, dateOfBirth: String) : Boolean
        fun signUp(fullName: String, email: String, password: String, phone: String, dateOfBirth: String)
    }


}