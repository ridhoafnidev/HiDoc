package com.ismealdi.hidoc.view.auth.signup

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.extensions.toDate
import com.ismealdi.hidoc.utils.extensions.user
import com.ismealdi.hidoc.utils.services.AmMessagingService

/**
 * Created by Al
 * on 20/04/19 | 02:09
 */
class SignUpPresenter(private val view: SignUpContract.View, private val context: Context) : SignUpContract.Presenter {

    init {
        view.presenter = this
    }

    override fun checkSession() {

    }

    override fun validate(fullName: String, email: String, password: String, phone: String, dateOfBirth: String): Boolean {
        val passwordLengthMin = context.resources.getInteger(R.integer.validation_password_min)

        if (fullName.isNotEmpty() &&
            email.isNotEmpty() && 
            password.isNotEmpty() &&
            dateOfBirth.isNotEmpty() &&
            phone.isNotEmpty()) {

            if(password.length < passwordLengthMin) {
                val validation = String.format(context.getString(R.string.validation_min_length), context.getString(R.string.text_password), passwordLengthMin)

                view.onError(validation)
            }else {
                return true
            }

        }else{
            view.onError(context.getString(R.string.validation_required_all_input))
        }

        return false
    }

    override fun signUp(fullName: String, email: String, password: String, phone: String, dateOfBirth: String) {
        view.progress(true)

        App.amAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) {
                App.amAuth.currentUser?.let { fireBaseUser ->
                    storeEmailVerification(fireBaseUser)

                    val user = User()

                    user.uid = fireBaseUser.uid
                    user.fullName = fullName
                    user.email = email
                    user.phoneNumber = phone
                    user.dateOfBirth = dateOfBirth.toDate()
                    user.category = Constants.USER.PATIENT

                    storeUser(user)
                }
            }else{
                view.onError(it.exception?.message.toString())
            }
        }
    }

    private fun storeEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                view.onInfo(String.format(context.getString(R.string.info_email_verification), user.email))
            } else {
                view.onError(it.exception?.message.toString())
            }
        }
    }

    private fun storeUser(user: User) {
        App.amDatabase.user(user.uid).set(user).addOnSuccessListener {
            signIn(user)
        }.addOnFailureListener {
            view.onError(it.message ?: "")
        }
    }

    private fun signIn(user: User) {
        AmMessagingService().storeOnline(true)
        AmMessagingService().storeToken(Preferences(context).getToken())
        App.amMessaging.subscribeToTopic(context.getString(R.string.app_channel))
        Preferences(context).storeUid(user.uid)

        view.showMain()
    }


}
