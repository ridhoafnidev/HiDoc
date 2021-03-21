package com.ismealdi.hidoc.view.auth.login

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.utils.interfaces.AmNetworkInterface

/**
 * Created by Al
 * on 06/04/19 | 20:59
 */
interface LoginContract {
	
	interface View : AmView<Presenter>, AmNetworkInterface {
		fun showMain()
	}
	
	interface Presenter : AmPresenter {
		fun checkSession()
		fun validate(email: String, password: String) : Boolean
		fun signIn(email: String, password: String)
	}
	
}