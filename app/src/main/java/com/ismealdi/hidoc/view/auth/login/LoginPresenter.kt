package com.ismealdi.hidoc.view.auth.login

import android.content.Context
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.services.AmMessagingService

/**
 * Created by Al
 * on 06/04/19 | 20:59
 */
class LoginPresenter(private val view: LoginContract.View, private val context: Context) :
    LoginContract.Presenter {

	init {
		view.presenter = this
	}
	
	override fun checkSession() {
		view.showMain()
	}

	override fun validate(email: String, password: String) : Boolean {

		if(email.isEmpty() && password.isEmpty()) {
			view.onError(context.getString(R.string.validation_required_all_input))

			return false
		}

		return true
	}

	override fun signIn(email: String, password: String) {
		view.progress(true)

		App.amAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
			view.progress(false)

			if(it.isSuccessful) {
				App.amAuth.currentUser?.let {user ->
					AmMessagingService().storeOnline(true)
					AmMessagingService().storeToken(Preferences(context).getToken())
					App.amMessaging.subscribeToTopic(context.getString(R.string.app_channel))
					Preferences(context).storeUid(user.uid)

					view.showMain()
				}
			}else{
				view.onError(it.exception?.message.toString())
			}
		}
	}
	
}