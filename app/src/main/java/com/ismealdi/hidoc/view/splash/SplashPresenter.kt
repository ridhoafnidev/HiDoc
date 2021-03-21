package com.ismealdi.hidoc.view.splash

import android.content.Context
import com.ismealdi.hidoc.App

/**
 * Created by Al
 * on 06/04/19 | 20:59
 */
class SplashPresenter(private val view: SplashContract.View, private val context: Context) : SplashContract.Presenter {
	
	init {
		view.presenter = this
	}
	
	override fun checkSession() {
		App.amAuth.currentUser?.let {
			view.showMain()
		} ?: run {
			view.showSignIn()
		}
	}
	
}