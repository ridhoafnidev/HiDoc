package com.ismealdi.hidoc.view.user.edit

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.extensions.toInputFormat
import kotlinx.android.synthetic.main.activity_user_edit_profile.*
import java.util.*

class UserEditActivity: AmActivity(R.layout.activity_user_edit_profile), UserEditContract.View {

    override var presenter: UserEditContract.Presenter? = UserEditPresenter(this, this)

    private lateinit var datePicker: DatePickerDialog

    private var uriPhoto: Uri? = null
    private var user: User = User()

    override fun initView() {
        initToolbar(back = true)
        setPageName(getString(R.string.title_edit_profile), false)
        initCalendarDialog()

        presenter?.user()
    }

    override fun displayUser(user: User) {
        val context = this
        this.user = user
        with(user) {
            inputEmailAddress?.setText(email)
            inputFullName?.setText(fullName)
            inputPhoneNumber?.setText(phoneNumber)
            inputBlood?.setText(blood)
            inputDateOfBirth?.setText(dateOfBirth.toInputFormat())

            imageProfile?.let { Utils().imageCircle(it, photoUrl, context) }

            if(sex == "Female") {
                radioFemale?.isChecked = true
            } else {
                radioMale?.isChecked = true
            }
        }

    }

    override fun initListener() {
        super.initListener()

        buttonUpdate.setOnClickListener {
            presenter?.let {

                val email = inputEmailAddress.text.toString()
                val password = inputPassword.text.toString()
                val name = inputFullName.text.toString()
                val phone = inputPhoneNumber.text.toString()
                val blood = inputBlood.text.toString()
                val dateOfBirth = inputDateOfBirth.text.toString()
                val sex = if(radioMale.isChecked) radioMale.text.toString() else radioFemale.text.toString()

                if(it.validate(name, email, password, phone, dateOfBirth, sex, blood)) {
                    it.edit(name, email, password, phone, dateOfBirth, sex, blood, uriPhoto, user)
                }
            }
        }

        inputDateOfBirth.setOnClickListener {
            datePicker.show()
        }

        buttonChangePhoto.setOnClickListener {
            photoPicker()
        }

    }

    private fun photoPicker() {
        val permissionCheckStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheckStorage != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.INTENT.REQUEST.PERMISSION)
        } else {
            presenter?.picker(packageManager, this)
        }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == Constants.INTENT.REQUEST.PERMISSION) {
            photoPicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let { intentData ->

            if(requestCode == Constants.INTENT.REQUEST.IMAGE_PICKER && resultCode == RESULT_OK) {
                progress(true)

                imageProfile?.let { Utils().imageCircle(it, intentData.data?.toString(), this) }

                Handler().postDelayed({
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                    uriPhoto = Utils().getImageUri(applicationContext, Utils().compressBitmap(bitmap, 10))

                    progress(false)
                }, 1500)

            }
        }
    }

}