package com.ismealdi.hidoc

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.storage.FirebaseStorage


@SuppressLint("StaticFieldLeak")
class App : MultiDexApplication() {
	
	companion object {
		lateinit var amAuth: FirebaseAuth
		lateinit var amDatabase: FirebaseFirestore
		lateinit var amStorage: FirebaseStorage
		lateinit var amMessaging: FirebaseMessaging
		lateinit var amPerformance: FirebasePerformance
	}
	
	override fun onCreate() {
		super.onCreate()
		
		amAuth = FirebaseAuth.getInstance()
		amDatabase = FirebaseFirestore.getInstance()
		amStorage = FirebaseStorage.getInstance()
		amMessaging = FirebaseMessaging.getInstance()
		amPerformance = FirebasePerformance.getInstance()
	}
	
	override fun attachBaseContext(base: Context) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}
}