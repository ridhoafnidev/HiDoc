package com.ismealdi.hidoc.view.auth.forgot

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.utils.interfaces.AmNetworkInterface

/**
 * Created by Al
 * on 20/04/19 | 02:08
 */
interface ForgotPasswordContract {
    interface View : AmView<Presenter>, AmNetworkInterface {
        fun clearForm()
    }

    interface Presenter : AmPresenter {
        fun resetPassword(email: String)
    }


}