package com.ismealdi.hidoc.view.auth.forgot

import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

/**
 * Created by Al
 * on 20/04/19 | 02:07
 */
class ForgotPasswordActivity: AmActivity(R.layout.activity_forgot_password), ForgotPasswordContract.View {

    override var presenter: ForgotPasswordContract.Presenter? =
        ForgotPasswordPresenter(this, this)
    
    override fun initView() {
        initToolbar(pageName = getString(R.string.title_forgot_password), back = true)
    }

    override fun initListener() {
        super.initListener()

        buttonReset.setOnClickListener {
            presenter?.resetPassword(inputEmailAddress.text.toString())
        }
    }

    override fun clearForm() {
        inputEmailAddress.setText("")
    }
}