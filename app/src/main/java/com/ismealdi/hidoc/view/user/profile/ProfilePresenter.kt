package com.ismealdi.hidoc.view.user.profile

import android.content.Context
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.extensions.user
import com.ismealdi.hidoc.utils.services.AmMessagingService

class ProfilePresenter(private val view: ProfileContract.View, private val context: Context) : ProfileContract.Presenter {

    override fun signOut() {
        AmMessagingService().storeOnline(false)
        App.amMessaging.unsubscribeFromTopic(context.getString(R.string.app_channel))
        App.amAuth.signOut()
        Preferences(context).storeUid("")
        Preferences(context).storeToken("")
        view.showSignIn()
    }

    override fun user() {
        App.amAuth.currentUser?.let {
            App.amDatabase.user(it.uid).addSnapshotListener { documentSnapshot, fireBaseFireStoreException ->

                documentSnapshot?.let { document ->
                    document.toObject(User::class.java)?.let { user ->
                        view.displayUserInfo(user)
                    }
                }

                fireBaseFireStoreException ?.let { exception ->
                    view.onError(exception.localizedMessage.toString())
                }

            }
        }
    }


}