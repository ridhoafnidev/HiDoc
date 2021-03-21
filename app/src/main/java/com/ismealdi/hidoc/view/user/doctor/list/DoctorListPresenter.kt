package com.ismealdi.hidoc.view.user.doctor.list

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentSnapshot
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.Favorite
import com.ismealdi.hidoc.struct.Specialist
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.extensions.doctors
import com.ismealdi.hidoc.utils.extensions.favorite
import com.ismealdi.hidoc.utils.extensions.specialists

class DoctorListPresenter(
    private val view: DoctorListContract.View,
    private val context: Context,
    private val isFavorite: Boolean = false
) :
    DoctorListContract.Presenter {

    init {
        view.presenter = this
    }

    private val doctors = mutableListOf<User>()
    private val favorites = mutableListOf<Favorite>()
    private val specialists = hashMapOf<String, String>()
    private var friends: HashSet<String> = hashSetOf()

    private val onFailureListener = OnFailureListener {
        view.onError(it.message.toString())
    }

    private fun specialists() {
        App.amDatabase.specialists().get().addOnSuccessListener {
            it.documents.forEach { document: DocumentSnapshot? ->
                document?.let {
                    document.toObject(Specialist::class.java)?.let { item ->
                        specialists[it.id] = item.name
                    }
                }
            }

            getFavorites()
        }.addOnFailureListener(onFailureListener)
    }

    override fun favorites() {
        specialists()
    }

    private fun getFavorites() {
        App.amAuth.currentUser?.let { user ->
            App.amDatabase.favorite().whereEqualTo(Favorite.Companion.Fields.owner, user.uid)
                .get().addOnSuccessListener {
                    it.documents.forEach { document: DocumentSnapshot? ->
                        document?.let {
                            document.toObject(Favorite::class.java)?.let { item ->
                                favorites.add(item)
                                friends.add(item.friend)
                            }
                        }
                    }

                    view.displayFavorites(favorites)
                    doctors()

                }.addOnFailureListener(onFailureListener)
        }
    }

    override fun doctors() {
        val query = App.amDatabase.doctors()

        query.get().addOnSuccessListener {
            it.documents.forEach { document: DocumentSnapshot? ->
                document?.let {
                    document.toObject(User::class.java)?.let { item ->
                        if(item.uid != Preferences(context).getUid() &&
                            friends.contains(item.uid)) {
                            val specialistName = specialistName(item.specialist)
                            item.specialist = specialistName
                            doctors.add(item)
                        }
                    }
                }
            }

            view.displayDoctors(doctors)
        }.addOnFailureListener(onFailureListener)
    }

    override fun add(position: Int, doctor: User) {
        App.amAuth.currentUser?.let {
            val favorite = Favorite(it.uid, doctor.uid, doctor.pushId, doctor.fullName)

            App.amDatabase.favorite()
                .whereEqualTo(Favorite.Companion.Fields.friend, favorite.friend)
                .whereEqualTo(Favorite.Companion.Fields.owner, favorite.owner)
                .limit(1).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let { query ->
                            if (query.isEmpty) {
                                addFavorite(position, favorite)
                            }else{
                                view.onError(context.getString(R.string.info_duplicate_data))
                                view.refreshDoctor(position)
                            }
                        }
                    }

                }
        }
    }

    private fun addFavorite(position: Int, favorite: Favorite) {
        App.amDatabase.favorite().add(favorite).addOnSuccessListener {
            favorites.add(favorite)
            view.displayFavorites(favorites)
            view.refreshDoctor(position)
        }.addOnFailureListener {
            view.onError(it.message.toString())
            view.refreshDoctor(position)
        }
    }

    private fun specialistName(specialist: String): String {
        return specialists[specialist] ?: ""
    }


}