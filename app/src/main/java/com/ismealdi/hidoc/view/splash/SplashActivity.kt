package com.ismealdi.hidoc.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.view.animation.AnimationUtils
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.utils.services.AmMessagingService
import com.ismealdi.hidoc.utils.services.AmTaskService
import com.ismealdi.hidoc.view.auth.login.LoginActivity
import com.ismealdi.hidoc.view.user.UserMainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Created by Al
 * on 06/04/19 | 20:48
 */
class SplashActivity: AmActivity(R.layout.activity_splash), SplashContract.View {
	
	override var presenter: SplashContract.Presenter? = SplashPresenter(this, this)
	
	private var timer = Timer()
	
	override fun initView() {
		splashAnimation()
	}
	
	private fun splashAnimation() {
		var count = 0
		
		timer.scheduleAtFixedRate(timerTask {
			ssConfiguration(count++)
			
			if(count > 5) {
				timer.cancel()
			}
			
		}, 0, 400)
	}
	
	@SuppressLint("ResourceType")
	private fun ssConfiguration(count: Int) {
		when (count) {
			0 -> runOnUiThread {
				val animate = AnimationUtils.loadAnimation(this, R.animator.bounce)
				imageLogo.animation = animate
			}
			5 -> {
				runOnUiThread {
					presenter?.checkSession()
				}
			}
			
			else -> return
		}
	}
	
	override fun onBackPressed() {
		timer.cancel()
		finish()
	}
	
	override fun showMain() {
		val intent = Intent(this, UserMainActivity::class.java)

		startService(Intent(baseContext, AmTaskService::class.java))

		AmMessagingService().storeOnline(true)

		startActivity(intent)
		finish()
	}

	override fun showSignIn() {
		val intent = Intent(this, LoginActivity::class.java)

		startActivity(intent)
		finish()
	}
	
}