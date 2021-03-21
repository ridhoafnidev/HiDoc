package com.ismealdi.hidoc.view.user.edit

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.User

interface UserEditContract {

    interface View : AmView<Presenter> {

        fun displayUser(user: User)

    }

    interface Presenter : AmPresenter {

        fun validate(fullName: String, email: String, password: String, phone: String, dateOfBirth: String, sex: String, bloodType: String) : Boolean
        fun edit(fullName: String, email: String, password: String, phone: String, dateOfBirth: String, sex: String, bloodType: String, uriPhoto: Uri?, user: User)
        fun picker(packageManager: PackageManager, activity: Activity)
        fun user()

    }

}