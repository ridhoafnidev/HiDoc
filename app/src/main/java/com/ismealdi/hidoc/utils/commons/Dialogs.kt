package com.ismealdi.hidoc.utils.commons

import android.content.Context
import android.support.v4.content.ContextCompat
import com.ismealdi.hidoc.R
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * Created by Al
 * on 21/04/19 | 00:58
 */
class Dialogs {

    fun initProgressDialog(context: Context): KProgressHUD {
        return KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setDimAmount(0.2f)
            .setCornerRadius(4f)
            .setSize(45,45)
            .setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .setAnimationSpeed(2)
    }


}