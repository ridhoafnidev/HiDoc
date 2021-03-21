package com.ismealdi.hidoc.view.user.profile

import android.content.Intent
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmFragment
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.view.auth.login.LoginActivity
import com.ismealdi.hidoc.view.user.edit.UserEditActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : AmFragment(R.layout.fragment_profile, R.string.title_profile), ProfileContract.View {

	override var presenter: ProfileContract.Presenter? = null

	override fun initView() {
		presenter = amActivity?.let {
			ProfilePresenter(
				this,
				it
			)
		}

		presenter?.user()
	}

	override fun displayUserInfo(user: User) {
		labelName?.text = user.fullName
		labelPhone?.text = user.phoneNumber
		ratingBar?.rating = user.rating
		labelEmail?.text = user.email
		labelBloodType?.text = user.blood
		labelSex?.text = user.sex

		activity?.let { context ->
			imageProfilePicture?.let { Utils().imageCircle(it, user.photoUrl, context) }
		}

	}

	override fun showSignIn() {
		activity?.let {
			startActivity(Intent(it, LoginActivity::class.java))
			it.finish()
		}
	}

	override fun initListener() {
		buttonSignOut.setOnClickListener {
			presenter?.signOut()
		}

		buttonEdit.setOnClickListener {
			activity?.let {
				startActivity(Intent(it, UserEditActivity::class.java))
			}
		}
	}
	
}