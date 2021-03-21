package com.ismealdi.hidoc.view.auth.login

import android.content.Intent
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.view.auth.forgot.ForgotPasswordActivity
import com.ismealdi.hidoc.view.auth.signup.SignUpActivity
import com.ismealdi.hidoc.view.user.UserMainActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

/**
 * Created by Al
 * on 06/04/19 | 20:48
 */
class LoginActivity: AmActivity(R.layout.activity_sign_in), LoginContract.View {

	override var presenter: LoginContract.Presenter? =
		LoginPresenter(this, this)

	override fun initView() {
		
	}

	override fun initListener() {
		
		buttonForgotPassword.setOnClickListener {
			showForgotPassword()
		}

		buttonRegister.setOnClickListener { 
			showSignUp()
		}
		
		buttonLogIn.setOnClickListener {
			presenter?.let {
				val email = inputEmailAddress.text.toString()
				val password = inputPassword.text.toString()

				if(it.validate(email, password)) {
					it.signIn(email, password)
				}
			}
		}
	}

	override fun showMain() {
		val intent = Intent(this, UserMainActivity::class.java)

		startActivity(intent)
		finish()
	}
	
	private fun showSignUp() {
		val intent = Intent(this, SignUpActivity::class.java)
		startActivity(intent)
	}
	
	private fun showForgotPassword() {
		val intent = Intent(this, ForgotPasswordActivity::class.java)
		startActivity(intent)
	}

}
