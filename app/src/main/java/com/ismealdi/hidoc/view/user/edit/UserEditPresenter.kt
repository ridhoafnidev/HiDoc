package com.ismealdi.hidoc.view.user.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.Timestamp
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.REQUEST.IMAGE_PICKER
import com.ismealdi.hidoc.utils.commons.Constants.PATH.PROFILE_PHOTO
import com.ismealdi.hidoc.utils.extensions.toDate
import com.ismealdi.hidoc.utils.extensions.user

class UserEditPresenter(private val view: UserEditContract.View, private val context: Context) : UserEditContract.Presenter {

    init {
        view.presenter = this
    }

    private val onFailureListener = OnFailureListener {
        view.onError(it.message.toString())
    }

    override fun validate(fullName: String, email: String, password: String, phone: String, dateOfBirth: String, sex: String, bloodType: String): Boolean {

        if(fullName.isBlank() || email.isBlank() ||
            dateOfBirth.isBlank() || phone.isBlank() ||
            sex.isBlank() || bloodType.isBlank()) {
            view.onError(context.getString(R.string.validation_required_all_input))

            return false
        }

        return true
    }

    override fun edit(fullName: String, email: String, password: String, phone: String, dateOfBirth: String, sex: String, bloodType: String, uriPhoto: Uri?, user: User) {
        App.amAuth.currentUser?.let { fireBaseUser ->

            user.uid = fireBaseUser.uid
            user.email = email
            user.fullName = fullName
            user.phoneNumber = phone
            user.blood = bloodType
            user.sex = sex
            user.dateOfBirth = dateOfBirth.toDate()

            uriPhoto?.let {
                upload(user, it)
            } ?: run {
                update(user)
            }
        }

    }

    private fun upload(user: User, uriPhoto: Uri) {
        view.progress(true)
        val storageRef = App.amStorage.reference.child(PROFILE_PHOTO + user.uid)
        val uploadTask = storageRef.putFile(uriPhoto)
        uploadTask.addOnSuccessListener { _ ->
            storageRef.downloadUrl.addOnSuccessListener {
                user.photoUrl = it.toString()
                update(user)
            }.addOnFailureListener(onFailureListener)

        }.addOnFailureListener(onFailureListener)
    }

    override fun picker(packageManager: PackageManager, activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        intent.resolveActivity(packageManager)?.let {
            activity.startActivityForResult(intent, IMAGE_PICKER)
        }
    }

    override fun user() {
        App.amAuth.currentUser?.let {
            App.amDatabase.user(it.uid).addSnapshotListener { documentSnapshot, fireBaseFireStoreException ->

                documentSnapshot?.let { document ->
                    document.toObject(User::class.java)?.let { user ->
                        view.displayUser(user)
                    }
                }

                fireBaseFireStoreException ?.let { exception ->
                    view.onError(exception.localizedMessage.toString())
                }

            }
        }

    }

    private fun update(user: User) {
        view.progress(true)
        user.lastUpdated = Timestamp.now()

        App.amDatabase.user(user.uid).set(user)
        .addOnSuccessListener {
            view.onSuccess(context.getString(R.string.info_profile_updated))
        }.addOnFailureListener(onFailureListener)
    }


}