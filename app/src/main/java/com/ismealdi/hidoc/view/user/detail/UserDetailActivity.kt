package com.ismealdi.hidoc.view.user.detail

import android.content.Intent
import android.view.Gravity
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.view.user.conversation.ConversationActivity
import kotlinx.android.synthetic.main.activity_user_detail_profile.*

class UserDetailActivity: AmActivity(R.layout.activity_user_detail_profile), UserDetailContract.View {

    override var presenter: UserDetailContract.Presenter? = UserDetailPresenter(this, this)

    private var user: User? = null

    override fun initView() {
        initToolbar(back = true)
        setPageName(getString(R.string.text_doctor), false)
        setTitleGravity(Gravity.CENTER)

        user = intent.getParcelableExtra(Constants.INTENT.ACTIVITY.USER)

        user?.let { userData ->
            displayUserInformation(userData)
        }
    }

    override fun initListener() {
        super.initListener()

        buttonChat.setOnClickListener {
            val intent = Intent(this, ConversationActivity::class.java)
            intent.putExtra(Constants.INTENT.ACTIVITY.USER, user)
            startActivity(intent)
        }
    }

    override fun displayUserInformation(user: User) {
        labelName?.text = user.fullName
        labelPhone?.text = user.phoneNumber
        ratingBar?.rating = user.rating
        labelEmail?.text = user.email
        labelBloodType?.text = user.blood
        labelSex?.text = user.sex

        imageUserPicture?.let { Utils().imageCircle(it, user.photoUrl, this) }

    }



}