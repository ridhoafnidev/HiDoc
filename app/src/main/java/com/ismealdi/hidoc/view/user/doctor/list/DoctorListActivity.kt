package com.ismealdi.hidoc.view.user.doctor.list

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.struct.Favorite
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.ACTIVITY.FAV
import com.ismealdi.hidoc.view.user.conversation.ConversationActivity
import com.ismealdi.hidoc.view.user.detail.UserDetailActivity
import com.ismealdi.hidoc.view.user.doctor.list.adapter.DoctorGridAdapter
import com.ismealdi.hidoc.view.user.home.adapter.doctor.DoctorAdapterInterface
import kotlinx.android.synthetic.main.activity_doctor_list.*
import java.util.*

class DoctorListActivity: AmActivity(R.layout.activity_doctor_list), DoctorListContract.View,
    DoctorAdapterInterface {

    override var presenter: DoctorListContract.Presenter? = null

    private var doctorAdapter: DoctorGridAdapter? = null

    private var favorites: HashSet<String> = hashSetOf()

    override fun initView() {
        val isFav = intent.getBooleanExtra(FAV, false)

        presenter = DoctorListPresenter(this, this, isFavorite = isFav)
        initToolbar(back = true)
        setPageName(getString(R.string.title_doctors), line = false)
        setTitleGravity(Gravity.CENTER)

        doctorAdapter = DoctorGridAdapter(mutableListOf(), hashSetOf(), this, this)

        lists.layoutManager = GridLayoutManager(this, 2)
        lists.adapter = doctorAdapter

        presenter?.favorites()
    }

    override fun displayDoctors(doctors: List<User>) {
        doctorAdapter?.update(doctors)
    }

    override fun displayFavorites(favorite: List<Favorite>) {
        favorites.clear()
        favorite.forEach {
            favorites.add(it.friend)
        }
    }

    override fun onPlusClick(position: Int, user: User) {
        presenter?.add(position, user)
    }

    override fun onChatClick(position: Int, user: User) {
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra(Constants.INTENT.ACTIVITY.USER, user)
        startActivity(intent)
    }

    override fun onDoctorClick(user: User) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra(Constants.INTENT.ACTIVITY.USER, user)
        startActivity(intent)
    }

    override fun refreshDoctor(position: Int) {
        doctorAdapter?.refresh(position, favorites)
    }


}