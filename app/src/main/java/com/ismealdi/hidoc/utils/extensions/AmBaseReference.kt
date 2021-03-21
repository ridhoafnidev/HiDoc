package com.ismealdi.hidoc.utils.extensions

import com.google.firebase.firestore.FirebaseFirestore
import com.ismealdi.hidoc.struct.*
import com.ismealdi.hidoc.utils.commons.Constants.USER.DOCTOR
import com.ismealdi.hidoc.utils.components.slider.AmImage

/**
 * Created by Al for Female Daily Network
 * on 22/11/18 | 17:58
 */

fun FirebaseFirestore.user() = collection(User.tableName)
fun FirebaseFirestore.user(uid: String) = collection(User.tableName).document(uid)

fun FirebaseFirestore.doctors() = collection(User.tableName).whereEqualTo(User.CREATOR.Fields.category, DOCTOR)

fun FirebaseFirestore.sliders() = collection(AmImage.tableName)

fun FirebaseFirestore.specialists() = collection(Specialist.tableName)

fun FirebaseFirestore.articles() = collection(Article.tableName)

fun FirebaseFirestore.location() = collection(Location.tableName)

fun FirebaseFirestore.favorite() = collection(Favorite.tableName)

fun FirebaseFirestore.chat() = collection(Chat.tableName)
fun FirebaseFirestore.chat(uid: String) = collection(Chat.tableName).document(uid)
fun FirebaseFirestore.conversation() = collection(Conversation.tableName)