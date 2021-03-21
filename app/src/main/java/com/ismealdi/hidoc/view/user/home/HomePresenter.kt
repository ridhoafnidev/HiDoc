package com.ismealdi.hidoc.view.user.home

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.struct.Favorite
import com.ismealdi.hidoc.struct.Specialist
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Preferences
import com.ismealdi.hidoc.utils.components.slider.AmImage
import com.ismealdi.hidoc.utils.extensions.*

class HomePresenter(private val view: HomeContract.View, private val context: Context) : HomeContract.Presenter {
	
	init {
		view.presenter = this
	}

	private val sliders = mutableListOf<AmImage>()
	private val specialists = hashMapOf<String, String>()
	private val articles = mutableListOf<Article>()
	private val doctors = mutableListOf<User>()
	private val favorites = mutableListOf<Favorite>()

	private val onFailureListener = OnFailureListener {
		view.onError(it.message.toString())
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

	override fun sliders() {
		App.amDatabase.sliders().limit(3).get().addOnSuccessListener {
			it.documents.forEach { document: DocumentSnapshot? ->
				document?.let {
					document.toObject(AmImage::class.java)?.let { image ->
						sliders.add(image)
					}
				}
			}

			view.displaySlider(sliders)
		}.addOnFailureListener(onFailureListener)
	}

	override fun specialists() {
		App.amDatabase.specialists().get().addOnSuccessListener {
			it.documents.forEach { document: DocumentSnapshot? ->
				document?.let {
					document.toObject(Specialist::class.java)?.let { item ->
						specialists[it.id] = item.name
					}
				}
			}

			view.displaySpecialists(specialists.values.toMutableList())
		}.addOnFailureListener(onFailureListener)
	}

	override fun articles() {
		App.amDatabase.articles().limit(3).orderBy(Article.CREATOR.Fields.createdOn, Query.Direction.DESCENDING).get().addOnSuccessListener {
			it.documents.forEach { document: DocumentSnapshot? ->
				document?.let {
					document.toObject(Article::class.java)?.let { item ->
						articles.add(item)
					}
				}
			}

			view.displayArticles(articles)
		}.addOnFailureListener(onFailureListener)
	}

	override fun doctors() {
		App.amDatabase.doctors().limit(4).get().addOnSuccessListener {
			it.documents.forEach { document: DocumentSnapshot? ->
				document?.let {
					document.toObject(User::class.java)?.let { item ->
						if(item.uid != Preferences(context).getUid()) {
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

	override fun favorites() {
		App.amAuth.currentUser?.let { user ->
			App.amDatabase.favorite().whereEqualTo(Favorite.Companion.Fields.owner, user.uid)
				.get().addOnSuccessListener {
					it.documents.forEach { document: DocumentSnapshot? ->
						document?.let {
							document.toObject(Favorite::class.java)?.let { item ->
								favorites.add(item)
							}
						}
					}

					view.displayFavorites(favorites)
					doctors()

				}.addOnFailureListener(onFailureListener)
		}
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