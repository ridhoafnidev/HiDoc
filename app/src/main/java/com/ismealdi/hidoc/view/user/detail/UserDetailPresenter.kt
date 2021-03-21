package com.ismealdi.hidoc.view.user.detail

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener

class UserDetailPresenter(private val view: UserDetailContract.View, private val context: Context) : UserDetailContract.Presenter {

    init {
        view.presenter = this
    }

    private val onFailureListener = OnFailureListener {
        view.onError(it.message.toString())
    }


}