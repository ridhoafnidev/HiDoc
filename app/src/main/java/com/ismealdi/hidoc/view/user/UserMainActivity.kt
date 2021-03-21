package com.ismealdi.hidoc.view.user

import android.content.Intent
import android.support.annotation.MenuRes
import android.support.design.widget.BottomNavigationView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.base.AmFragment
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.extensions.selectedListener
import com.ismealdi.hidoc.utils.services.AmMessagingService
import com.ismealdi.hidoc.view.user.chat.ChatFragment
import com.ismealdi.hidoc.view.user.doctor.list.DoctorListActivity
import com.ismealdi.hidoc.view.user.home.HomeFragment
import com.ismealdi.hidoc.view.user.profile.ProfileFragment
import com.ismealdi.hidoc.view.user.scanner.ScannerActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.component_toolbar.*

class UserMainActivity : AmActivity(R.layout.activity_main), BottomNavigationView.OnNavigationItemSelectedListener {
	
	private val home = HomeFragment()
	private val profile = ProfileFragment()
	private val chat = ChatFragment()
	
	private val fragmentManager = supportFragmentManager
	private var activeFragment : AmFragment = home
	
	override fun initView() {
		initToolbar(pageName = getString(R.string.title_home))
		initBottomNavigation()
		initFragment()
	}
	
	override fun initListener() {
		componentBottomMenu selectedListener(this)
	}
	
	private fun initBottomNavigation() {
		componentBottomMenu.itemIconTintList = null
	}
	
	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.home, menu)
		
		return true
	}
	
	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		resetMenu()
		
		val state = when (item.itemId) {
			R.id.home -> {
				fragmentManager.beginTransaction().hide(activeFragment).show(home).commit()
				activeFragment = home
				appendMenu(R.menu.home)
				
				true
			}
			
			R.id.profile -> {
				supportActionBar?.let { actionBar ->
					actionBar.setDisplayHomeAsUpEnabled(true)
					actionBar.setHomeAsUpIndicator(R.drawable.ic_qr)
				}
				
				fragmentManager.beginTransaction().hide(activeFragment).show(profile).commit()
				activeFragment = profile
				appendMenu(R.menu.profile)
				
				true
			}
			
			R.id.chat -> {
				fragmentManager.beginTransaction().hide(activeFragment).show(chat).commit()
				activeFragment = chat
				appendMenu(R.menu.chat)
				setToolbarSearch(true)
				
				true
			}
			
			else -> {
				false
			}
		}
		
		if(item.itemId == R.id.profile) {
			activeFragment.setTitle(gravity = Gravity.CENTER)
		}else{
			activeFragment.setTitle(gravity = Gravity.START)
		}
		
		return state
	}
	
	private fun appendMenu(@MenuRes menu: Int) {
		toolbar.inflateMenu(menu)
	}
	
	private fun resetMenu() {
		setToolbarSearch(false)
		supportActionBar?.let { actionBar ->  
			actionBar.setDisplayHomeAsUpEnabled(false)
			actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron)
		}
		
		toolbar.menu.removeItem(R.id.schedule)
		toolbar.menu.removeItem(R.id.love)
	}
	
	private fun initFragment() {
		fragmentManager.beginTransaction().add(R.id.frameLayout, profile, Constants.FRAGMENT.PROFILE.NAME).hide(profile).commit()
		fragmentManager.beginTransaction().add(R.id.frameLayout, chat, Constants.FRAGMENT.CHAT.NAME).hide(chat).commit()
		fragmentManager.beginTransaction().add(R.id.frameLayout, home, Constants.FRAGMENT.HOME.NAME).commit()
		
		componentBottomMenu.selectedItemId = R.id.home
	}

	override fun onBackPressed() {
		AmMessagingService().storeOnline(false)
		super.onBackPressed()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {

		when (item?.itemId) {
			R.id.love -> {

				val intent = Intent(this, DoctorListActivity::class.java)
				intent.putExtra(Constants.INTENT.ACTIVITY.FAV, true)
				startActivity(intent)

			}

			android.R.id.home -> {

				val intent = Intent(this, ScannerActivity::class.java)
				intent.putExtra(Constants.INTENT.ACTIVITY.FAV, true)
				startActivity(intent)

			}
		}

		return true
	}
}
