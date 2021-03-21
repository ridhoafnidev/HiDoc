package com.ismealdi.hidoc.view.auth.forgot

import android.content.Context
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R

/**
 * Created by Al
 * on 20/04/19 | 02:09
 */
class ForgotPasswordPresenter(private val view: ForgotPasswordContract.View, private val context: Context) :

    ForgotPasswordContract.Presenter {

    init {
        view.presenter = this
    }

    override fun resetPassword(email: String) {
        if (email.isEmpty()) {
            view.onError(context.getString(R.string.validation_email_reset))
        }else{
            view.progress(true)
            App.amAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                view.onInfo(String.format(context.getString(R.string.info_email_reset), email))
            }.addOnFailureListener {
                view.onError(it.localizedMessage.toString())
            }

            view.clearForm()
        }
    }

}