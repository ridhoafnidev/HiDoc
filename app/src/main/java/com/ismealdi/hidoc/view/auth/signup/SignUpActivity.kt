package com.ismealdi.hidoc.view.auth.signup

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.ACTIVITY.FIRST_LOGIN
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.REQUEST.IMAGE_PICKER
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.view.user.UserMainActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.inputDateOfBirth
import kotlinx.android.synthetic.main.activity_sign_up.inputEmailAddress
import kotlinx.android.synthetic.main.activity_sign_up.inputFullName
import kotlinx.android.synthetic.main.activity_sign_up.inputPassword
import kotlinx.android.synthetic.main.activity_sign_up.inputPhoneNumber
import kotlinx.android.synthetic.main.activity_user_edit_profile.*
import java.util.*

/**
 * Created by Al
 * on 20/04/19 | 02:07
 */
class SignUpActivity: AmActivity(R.layout.activity_sign_up), SignUpContract.View {

    override var presenter: SignUpContract.Presenter? = SignUpPresenter(this, this)
    
    private lateinit var datePicker: DatePickerDialog
    private var uriPhoto: Uri? = null
    
    override fun initView() {
        initCalendarDialog()
        initToolbar(back = true)
        setPageName("", false)

        presenter?.checkSession()
    }

    override fun initListener() {
        buttonJoin.setOnClickListener {
            presenter?.let {
                val fullName = inputFullName.text.toString()
                val email = inputEmailAddress.text.toString()
                val password = inputPassword.text.toString()
                val phone = inputPhoneNumber.text.toString()
                val dateOfBirth = inputDateOfBirth.text.toString()

                if(it.validate(fullName, email, password, phone, dateOfBirth)) {
                    it.signUp(fullName, email, password, phone, dateOfBirth)
                }
            }
        }
        
        inputDateOfBirth.setOnClickListener {
            datePicker.show()
        }
    }

    override fun showMain() {
        val intent = Intent(this, UserMainActivity::class.java)

        intent.putExtra(FIRST_LOGIN, true)

        ActivityCompat.startActivity(this, intent, null)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finishAffinity()
    }
    
    private fun initCalendarDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->
            val mm = m + 1
            inputDateOfBirth.setText("$d/$mm/$y")
        }, year, month, day)

        datePicker.setOnCancelListener {
            inputDateOfBirth.setText("")
        }
    }
}